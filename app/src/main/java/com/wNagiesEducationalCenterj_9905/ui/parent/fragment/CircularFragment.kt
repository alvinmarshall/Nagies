package com.wNagiesEducationalCenterj_9905.ui.parent.fragment


import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.*
import com.wNagiesEducationalCenterj_9905.common.utils.FileTypeUtils
import com.wNagiesEducationalCenterj_9905.common.utils.PermissionAskListener
import com.wNagiesEducationalCenterj_9905.common.utils.PermissionUtils
import com.wNagiesEducationalCenterj_9905.ui.adapter.CircularAdapter
import com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel.StudentViewModel
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.fragment_circular.*
import org.jetbrains.anko.support.v4.toast
import java.io.File

class CircularFragment : BaseFragment() {
    private lateinit var studentViewModel: StudentViewModel
    private var adapter: CircularAdapter? = null
    private var downloadReceiver: BroadcastReceiver? = null
    private var downloadList: ArrayList<Triple<Int?, String?, Long?>> = ArrayList()
    private var itemData: Triple<ViewFilesAction, Int?, String?>? = null
    private var snackBar: Snackbar? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_circular, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = recycler_view
        snackBar = Snackbar.make(root, "", Snackbar.LENGTH_SHORT)
        downloadReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                downloadList.forEach {
                    if (it.third == id) {
                        studentViewModel.saveDownloadFilePathToDb(
                            it.first,
                            it.second,
                            DBEntities.CIRCULAR
                        )
                    }
                }
            }
        }
        context?.registerReceiver(
            downloadReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        configureViewModel()
    }

    private fun initRecyclerView() {
        recyclerView?.hasFixedSize()
        recyclerView?.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        adapter = CircularAdapter()
        adapter?.setCallBack(object : ItemCallback<Triple<ViewFilesAction, Int?, String?>> {
            override fun onHold(data: Triple<ViewFilesAction, Int?, String?>?) {

            }

            override fun onClick(data: Triple<ViewFilesAction, Int?, String?>?) {
                when (data?.first) {
                    ViewFilesAction.VIEW -> {
                        itemData = data
                        context?.let {
                            PermissionUtils.checkPermission(
                                it,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, listener
                            )
                        }

                    }
                    ViewFilesAction.DOWNLOAD -> {
                        itemData = data
                        context?.let {
                            PermissionUtils.checkPermission(
                                it,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, listener
                            )
                        }
                    }
                    else -> {
                    }
                }

            }
        })
        recyclerView?.adapter = adapter
    }

    private fun configureViewModel() {
        studentViewModel =
            ViewModelProvider(this, viewModelFactory)[StudentViewModel::class.java]
        studentViewModel.getUserToken()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        studentViewModel.cachedToken.observe(viewLifecycleOwner, Observer { token ->
            studentViewModel.getCircularInformation(token)
                .observe(viewLifecycleOwner, Observer { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            showDataAvailableMessage(
                                label_msg_title,
                                resource.data,
                                MessageType.FILES
                            )
                            adapter?.submitList(resource.data)
                            showLoadingDialog(false)
                        }
                        Status.ERROR -> {
                            showDataAvailableMessage(
                                label_msg_title,
                                resource.data,
                                MessageType.FILES
                            )
                            showLoadingDialog(false)
                        }
                        Status.LOADING -> {
                            showLoadingDialog()
                        }
                    }
                })
        })
        studentViewModel.isSuccess.observe(viewLifecycleOwner, Observer {
            showDownloadComplete(it)
        })
    }

    private fun showLoadingDialog(show: Boolean = true) {
        showAnyView(progressBar, null, null, show) { view, _, _, visible ->
            if (visible) {
                (view as ProgressBar).visibility = View.VISIBLE
            } else {
                (view as ProgressBar).visibility = View.GONE
            }
        }
    }

    private fun showDownloadComplete(show: Boolean = true) {
        showAnyView(
            snackBar,
            getString(R.string.download_complete_message),
            null,
            show
        ) { view, msg, _, visible ->
            if (visible) {
                (view as Snackbar).setText(msg!!).show()
                adapter?.notifyDataSetChanged()
            } else {
                (view as Snackbar).setText("Download Started...").show()
            }
        }
    }

    private fun loadFile(path: String?) {
        if (itemData?.first == ViewFilesAction.VIEW) {
            path?.let {
                val file = File(it)
                if (file.exists()) {
                    val openIntent = Intent(Intent.ACTION_VIEW)
                    val url = FileProvider.getUriForFile(
                        context!!,
                        getString(R.string.file_provider_authority),
                        file
                    )
                    openIntent.setDataAndType(url, FileTypeUtils.getType(file.absolutePath))
                    openIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    val open = Intent.createChooser(openIntent, getString(R.string.chooser_title))
                    startActivity(open)
                }
            } ?: toast("file not downloaded")
        }
    }

    private fun fetchFileFromServer(url: String?) {

        if (itemData?.first == ViewFilesAction.DOWNLOAD) {
            url?.let { fileUrl ->
                val fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length)
                downloadList = downloadWithManager(fileUrl, fileName, itemData)
            }
        }
    }

    private fun downloadWithManager(
        url: String,
        filename: String,
        data: Triple<ViewFilesAction, Int?, String?>?
    ): ArrayList<Triple<Int?, String?, Long?>> {
        val downloadList: ArrayList<Triple<Int?, String?, Long?>> = ArrayList()
        val id = data?.second
        val path =
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                filename
            )
        if (path.exists()) {
            studentViewModel.saveDownloadFilePathToDb(id, path.absolutePath, DBEntities.CIRCULAR)
            return downloadList
        }
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(filename)
        request.setDescription("file downloading...")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
        val manager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
        val downloadId = manager?.enqueue(request)
        downloadList.add(Triple(id, path.absolutePath, downloadId))
        return downloadList
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(downloadReceiver)
    }

    //region Permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when (itemData?.first) {
                        ViewFilesAction.VIEW -> {
                            loadFile(itemData?.third)
                        }
                        ViewFilesAction.DOWNLOAD -> {
                            fetchFileFromServer(itemData?.third)
                        }
                        else -> {
                        }
                    }

                } else {
                    toast("Permission Denied")
                }
            }
        }
    }

    private val listener: PermissionAskListener
        get() = object : PermissionAskListener {
            override fun onPermissionPreviouslyDenied() {
                showStorageRational(
                    getString(R.string.permission_denied),
                    getString(R.string.permission_storage_explained)
                )
            }

            override fun onNeedPermission() {
                activity?.let { activity ->
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        REQUEST_EXTERNAL_STORAGE
                    )
                }
            }

            override fun onPermissionDisabled() {
                dialogForSettings(
                    getString(R.string.permission_denied),
                    getString(R.string.permission_storage_message)
                )
            }

            override fun onPermissionGranted() {
                when (itemData?.first) {
                    ViewFilesAction.VIEW -> {
                        itemData = Triple(ViewFilesAction.VIEW, itemData?.second, itemData?.third)
                        loadFile(itemData?.third)

                    }
                    ViewFilesAction.DOWNLOAD -> {
                        fetchFileFromServer(itemData?.third)
                    }
                    else -> {
                    }
                }
            }
        }
    //endregion
}
