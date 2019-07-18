package com.wNagiesEducationalCenterj_9905.ui.parent.fragment


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.common.REQUEST_EXTERNAL_STORAGE
import com.wNagiesEducationalCenterj_9905.common.showAnyView
import com.wNagiesEducationalCenterj_9905.common.utils.FileTypeUtils
import com.wNagiesEducationalCenterj_9905.common.utils.PermissionAskListener
import com.wNagiesEducationalCenterj_9905.common.utils.PermissionUtils
import com.wNagiesEducationalCenterj_9905.ui.adapter.AssignmentAdapter
import com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel.StudentViewModel
import com.wNagiesEducationalCenterj_9905.vo.DownloadRequest
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.fragment_assignment_pdf.*
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber
import java.io.File


class AssignmentPdfFragment : BaseFragment() {
    private lateinit var studentViewModel: StudentViewModel
    private var recyclerView: RecyclerView? = null
    private var assignmentAdapter: AssignmentAdapter? = null
    private var alertDialog: AlertDialog.Builder? = null
    private var callbackType: String? = null
    private var itemData: Pair<Int?, String?>? = null
    private var loadingIndicator: ProgressBar? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_assignment_pdf, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = recycler_view
        loadingIndicator = progressBar
        loadingIndicator?.visibility = View.GONE
        alertDialog = context?.let { AlertDialog.Builder(it) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        configureViewModel()
    }

    private fun initRecyclerView() {
        recyclerView?.hasFixedSize()
        recyclerView?.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        assignmentAdapter = AssignmentAdapter()
        assignmentAdapter?.setItemCallback(object : ItemCallback<Pair<Int?, String?>> {
            override fun onClick(data: Pair<Int?, String?>?) {
                itemData = data
                callbackType = "onClick"
                context?.let {
                    PermissionUtils.checkPermission(
                        it,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, listener
                    )
                }
            }

            override fun onHold(data: Pair<Int?, String?>?) {
                itemData = data
                callbackType = "onHold"
                context?.let {
                    PermissionUtils.checkPermission(
                        it,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, listener
                    )
                }
            }
        })
        recyclerView?.adapter = assignmentAdapter
    }

    private fun loadFile() {
        studentViewModel.downloadFilesFromServer(DownloadRequest(itemData?.second), itemData?.first)
        if (itemData?.second != null) {
            val file = File(itemData?.second!!)
            if (file.exists()) {
                val openIntent = Intent(Intent.ACTION_VIEW)
                val uri = FileProvider.getUriForFile(context!!,getString(R.string.file_provider_authority),file)
                openIntent.setDataAndType(uri, FileTypeUtils.getType(file.absolutePath))
                openIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                val open = Intent.createChooser(openIntent, getString(R.string.chooser_title))
                startActivity(open)
            }
        }

    }

    private fun showDeleteDialog() {
        alertDialog?.setTitle("Delete Alert")
        alertDialog?.setMessage("Do you want to delete this file ?")
        alertDialog?.setPositiveButton("yes") { dialog, _ ->
            studentViewModel.deleteAssignmentById(itemData?.first, itemData?.second)
            dialog.dismiss()
        }
        alertDialog?.setNegativeButton("cancel", null)
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }

    private fun configureViewModel() {
        studentViewModel = ViewModelProviders.of(this, viewModelFactory)[StudentViewModel::class.java]
        studentViewModel.getUserToken()
        studentViewModel.cachedToken.observe(viewLifecycleOwner, Observer { token ->
            studentViewModel.getStudentAssignmentPDF(token).observe(viewLifecycleOwner, Observer { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Timber.i("assignment files data :${resource.data?.size}")
                        showLoadingDialog(false)
                        assignmentAdapter?.submitList(resource?.data)
                    }
                    Status.ERROR -> {
                        Timber.i(resource.message)
                        showLoadingDialog(false)
                    }
                    Status.LOADING -> {
                        Timber.i("loading...")
                        showLoadingDialog()
                    }
                }
            })

        })
    }

    private fun showLoadingDialog(show: Boolean = true) {
        showAnyView(progressBar,null,null,show){view,_,_,visible ->
            if (visible){
                (view as ProgressBar).visibility = View.VISIBLE
            }else{
                (view as ProgressBar).visibility = View.GONE
            }
        }
    }


    //region Permission
    private fun showStorageRational(title: String, message: String) {
        alertDialog?.setTitle(title)
        alertDialog?.setMessage(message)
        alertDialog?.setPositiveButton("retry") { dialog, _ ->
            activity?.let {
                ActivityCompat.requestPermissions(
                    it, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_EXTERNAL_STORAGE
                )
            }
            dialog.dismiss()
        }
        alertDialog?.setNegativeButton("i'm sure", null)
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }

    private fun dialogForSettings(title: String, message: String) {
        alertDialog?.setTitle(title)
        alertDialog?.setMessage(message)
        alertDialog?.setPositiveButton("settings") { dialog, _ ->
            goToSettings()
            dialog.dismiss()
        }
        alertDialog?.setNegativeButton("not now", null)
        alertDialog?.setCancelable(false)
        alertDialog?.show()

    }

    private fun goToSettings() {
        val openAppSettings = Intent()
        openAppSettings.action = ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.parse("package:${context?.packageName}")
        openAppSettings.data = uri
        startActivity(openAppSettings)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when (callbackType) {
                        "onClick" -> {
                            loadFile()
                        }
                        "onHold" -> {
                            showDeleteDialog()
                        }
                    }

                } else {
                    toast("Permission Denied")
                }
            }
        }
    }

    val listener: PermissionAskListener
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
                when (callbackType) {
                    "onClick" -> {
                        loadFile()
                    }
                    "onHold" -> {
                        showDeleteDialog()
                    }
                }
            }
        }
    //endregion
}
