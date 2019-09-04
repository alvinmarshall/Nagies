package com.wNagiesEducationalCenterj_9905.ui.teacher.fragment


import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.api.request.ExplorerRequest
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.*
import com.wNagiesEducationalCenterj_9905.common.utils.PermissionAskListener
import com.wNagiesEducationalCenterj_9905.common.utils.PermissionUtils
import com.wNagiesEducationalCenterj_9905.ui.adapter.UploadFileAdapter
import com.wNagiesEducationalCenterj_9905.ui.teacher.viewmodel.TeacherViewModel
import kotlinx.android.synthetic.main.fragment_file_explorer.*
import org.jetbrains.anko.support.v4.toast
import java.io.File

class FileExplorerFragment : BaseFragment() {
    private lateinit var teacherViewModel: TeacherViewModel
    private var explorerType: String? = null
    private var adapter: UploadFileAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var alertDialog: AlertDialog.Builder? = null
    private var itemData: Triple<ViewFilesAction, Int?, String?>? = null
    private var snackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file_explorer, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        explorerType = arguments?.let { FileExplorerFragmentArgs.fromBundle(it).explorerType }
    }

    private fun getExplorerRequest(): ExplorerRequest {
        val type = explorerType?.split(" ")?.get(0)
        val format = explorerType?.split(" ")?.get(1)
        return ExplorerRequest(format, type)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alertDialog = context?.let { AlertDialog.Builder(it) }
        recyclerView = recycler_view
        snackBar = Snackbar.make(root, "", Snackbar.LENGTH_SHORT)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        configureViewModel()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_explorer, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                teacherViewModel.getUploadedFiles(getExplorerRequest())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDeleteDialog() {
        if (itemData?.first != ViewFilesAction.DELETE) {
            return
        }
        alertDialog?.setTitle("Delete Alert")
        alertDialog?.setMessage("Do you want to delete this file ?")
        alertDialog?.setPositiveButton("yes") { dialog, _ ->
            val req = getExplorerRequest()
            req.path = itemData?.third
            req.id = itemData?.second?.toString()
            teacherViewModel.deleteUploadedFiles(req)
            dialog.dismiss()
        }
        alertDialog?.setNegativeButton("cancel", null)
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }

    private fun fetchFileFromServer(url: String?) {
        if (itemData?.first == ViewFilesAction.DOWNLOAD) {
            url?.let { fileUrl ->
                val fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length)
                downloadWithManager(fileUrl, fileName)
            }
        }
    }

    private fun downloadWithManager(url: String, filename: String) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(filename)
        request.setDescription("file downloading...")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
        val manager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
        manager?.enqueue(request)
    }

    private fun configureViewModel() {
        teacherViewModel = ViewModelProviders.of(this, viewModelFactory)[TeacherViewModel::class.java]
        teacherViewModel.getUploadedFiles(getExplorerRequest())
        subscribeObservers()
    }

    private fun subscribeObservers() {
        teacherViewModel.isSuccess.observe(viewLifecycleOwner, Observer { success ->
            showLoadingDialog(success)
        })
        teacherViewModel.cachedUploadData.observe(viewLifecycleOwner, Observer { data ->
            showDataAvailableMessage(label_msg_title,data,MessageType.FILES)
            adapter?.submitList(data)
        })
        teacherViewModel.deleteUploadResponse.observe(viewLifecycleOwner, Observer { response ->
            snackBar?.setText(response.message)?.show()
        })
    }

    private fun initRecyclerView() {
        recyclerView?.hasFixedSize()
        recyclerView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = UploadFileAdapter()
        adapter?.setItemCallback(object : ItemCallback<Triple<ViewFilesAction, Int?, String?>> {
            override fun onClick(data: Triple<ViewFilesAction, Int?, String?>?) {
                itemData = data
                context?.let {
                    PermissionUtils.checkPermission(
                        it,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, listener
                    )
                }
            }

            override fun onHold(data: Triple<ViewFilesAction, Int?, String?>?) {
            }
        })
        recyclerView?.adapter = adapter
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

    //region Permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
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

                    }
                    ViewFilesAction.DOWNLOAD -> {
                        fetchFileFromServer(itemData?.third)
                    }
                    ViewFilesAction.DELETE -> {
                        showDeleteDialog()
                    }
                }
            }
        }
    //endregion
}
