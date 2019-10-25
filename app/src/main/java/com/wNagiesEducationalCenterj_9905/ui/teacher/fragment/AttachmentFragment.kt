package com.wNagiesEducationalCenterj_9905.ui.teacher.fragment


import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.api.request.FileUploadRequest
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.FILE_CHOOSER_RESULT
import com.wNagiesEducationalCenterj_9905.common.FileUploadFormat
import com.wNagiesEducationalCenterj_9905.common.REQUEST_EXTERNAL_STORAGE
import com.wNagiesEducationalCenterj_9905.common.UploadFileType
import com.wNagiesEducationalCenterj_9905.common.utils.FileTypeUtils
import com.wNagiesEducationalCenterj_9905.common.utils.PermissionAskListener
import com.wNagiesEducationalCenterj_9905.common.utils.PermissionUtils
import com.wNagiesEducationalCenterj_9905.common.utils.RealPathUtil
import com.wNagiesEducationalCenterj_9905.jobs.UploadFilesWorker
import com.wNagiesEducationalCenterj_9905.ui.teacher.viewmodel.TeacherViewModel
import kotlinx.android.synthetic.main.fragment_attachment.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber
import java.io.File


class AttachmentFragment : BaseFragment() {
    private lateinit var teacherViewModel: TeacherViewModel
    private var alertDialog: AlertDialog.Builder? = null
    private var snackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attachment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alertDialog = context?.let { AlertDialog.Builder(it) }
        snackBar = Snackbar.make(root, "", Snackbar.LENGTH_LONG)
        btn_upload_assignment.onClick {
            context?.let {
                PermissionUtils.checkPermission(
                    it,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    listener
                )
            }
        }
        btn_upload_report.onClick { openClassStudentList() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureViewModel()
    }

    private fun configureViewModel() {
        teacherViewModel = ViewModelProvider(this, viewModelFactory)[TeacherViewModel::class.java]
        subscribeObservers()
    }

    private fun subscribeObservers() {
        teacherViewModel.isSuccess.observe(viewLifecycleOwner, Observer {
            if (it) {
                snackBar?.setText(getString(R.string.upload_msg_success))?.show()
                snackBar?.dismiss()
            } else {
                snackBar?.setText(getString(R.string.upload_msg_starting))?.show()
            }
        })
    }

    private fun showUploadDialog(path: String?) {

        alertDialog?.setTitle(getString(R.string.upload_msg_title))
        alertDialog?.setMessage(getString(R.string.upload_msg_body))
        alertDialog?.setPositiveButton("upload") { dialog, _ ->
            dialog.dismiss()
            preparingToUpload(path)
        }
        alertDialog?.setNegativeButton("cancel", null)
        alertDialog?.show()
    }

    private fun openStorageMedia() {
        val fileIntent = Intent()
        fileIntent.action = Intent.ACTION_OPEN_DOCUMENT
        fileIntent.type = "*/*"
        startActivityForResult(fileIntent, FILE_CHOOSER_RESULT)
    }

    private fun openClassStudentList() {
        val action = AttachmentFragmentDirections.actionAttachmentFragmentToStudentListFragment()
        activity?.let { Navigation.findNavController(it, R.id.fragment_socket).navigate(action) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == FILE_CHOOSER_RESULT) {
            if (data != null) {
                val selectedFile = data.data
                var path: String? = null
                try {
                    selectedFile?.let { uri ->
                        path = context?.let { RealPathUtil.getRealPath(it, uri) }
                    }
                } catch (e: Exception) {
                    dialogsErrorMessage()
                }
                showUploadDialog(path)
            }
        }
    }

    private fun dialogsErrorMessage() {
        alertDialog?.setTitle(getString(R.string.storage_msg_invalid_title))
        alertDialog?.setMessage(getString(R.string.storage_msg_invalid_path))
        alertDialog?.setPositiveButton("ok", null)
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }

    private fun preparingToUpload(path: String?) {
        snackBar?.setText(getString(R.string.upload_msg_starting))?.show()
        context?.let { UploadFilesWorker.start(it, path, false, null) }
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
                    openStorageMedia()

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
                openStorageMedia()

            }
        }
    //endregion
}
