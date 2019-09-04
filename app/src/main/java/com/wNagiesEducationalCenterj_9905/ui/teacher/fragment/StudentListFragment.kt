package com.wNagiesEducationalCenterj_9905.ui.teacher.fragment


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.api.request.FileUploadRequest
import com.wNagiesEducationalCenterj_9905.api.request.StudentInfo
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.*
import com.wNagiesEducationalCenterj_9905.common.utils.FileTypeUtils
import com.wNagiesEducationalCenterj_9905.common.utils.PermissionAskListener
import com.wNagiesEducationalCenterj_9905.common.utils.PermissionUtils
import com.wNagiesEducationalCenterj_9905.common.utils.RealPathUtil
import com.wNagiesEducationalCenterj_9905.data.db.Entities.ClassStudentEntity
import com.wNagiesEducationalCenterj_9905.ui.adapter.ClassStudentAdapter
import com.wNagiesEducationalCenterj_9905.ui.teacher.viewmodel.TeacherViewModel
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.fragment_student_list.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber
import java.io.File

class StudentListFragment : BaseFragment() {
    private lateinit var teacherViewModel: TeacherViewModel
    private var adapter: ClassStudentAdapter? = null
    private var alertDialog: AlertDialog.Builder? = null
    private var snackBar: Snackbar? = null
    private var itemCallback: ClassStudentEntity? = null
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alertDialog = context?.let { AlertDialog.Builder(it) }
        snackBar = Snackbar.make(root, "", Snackbar.LENGTH_LONG)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_view.hasFixedSize()
        adapter = ClassStudentAdapter()
        adapter?.setItemCallback(object : ItemCallback<ClassStudentEntity> {
            override fun onClick(data: ClassStudentEntity?) {
                itemCallback = data
                context?.let {
                    PermissionUtils.checkPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE, listener)
                }
            }

            override fun onHold(data: ClassStudentEntity?) {
            }
        })
        recycler_view.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
        searchView = menu.findItem(R.id.action_search).actionView as? SearchView
        searchView?.isSubmitButtonEnabled
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                teacherViewModel.searchString.postValue(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                teacherViewModel.searchString.postValue(newText)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
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

    private fun dialogsErrorMessage() {
        alertDialog?.setTitle(getString(R.string.storage_msg_invalid_title))
        alertDialog?.setMessage(getString(R.string.storage_msg_invalid_path))
        alertDialog?.setPositiveButton("ok", null)
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }

    private fun preparingToUpload(path: String?) {
        if (path == null) return
        val file = File(path)
        var requestBody: MultipartBody.Part? = null

        val format = FileTypeUtils.getFileFormat(file.name)
        when (format) {
            FileUploadFormat.PDF -> {
                requestBody = MultipartBody.Part.createFormData(
                    getString(R.string.upload_form_file),
                    file.name,
                    RequestBody.create(MediaType.parse("application/pdf"), file)
                )
            }
            FileUploadFormat.IMAGE -> {
                requestBody = MultipartBody.Part.createFormData(
                    getString(R.string.upload_form_file),
                    file.name,
                    RequestBody.create(MediaType.parse("image/*"), file)
                )
            }
            null -> {
                toast("file not supported")
            }
        }
        val fileUploadRequest = FileUploadRequest(requestBody)
        val studentNo =
            MultipartBody.Part.createFormData(getString(R.string.report_form_student_no), itemCallback?.studentNo!!)
        val studentName =
            MultipartBody.Part.createFormData(getString(R.string.report_form_student_name), itemCallback?.studentName!!)
        fileUploadRequest.studentInfo = StudentInfo(studentNo, studentName)
        teacherViewModel.uploadFile(fileUploadRequest, format, UploadFileType.REPORT)
    }

    private fun configureViewModel() {
        teacherViewModel = ViewModelProviders.of(this, viewModelFactory)[TeacherViewModel::class.java]
        teacherViewModel.getUserToken()
        teacherViewModel.searchString.postValue("")
        teacherViewModel.userToken.observe(viewLifecycleOwner, Observer { token ->
            subscribeObservers(token)
        })
    }

    private fun subscribeObservers(token: String) {
        teacherViewModel.searchString.observe(viewLifecycleOwner, Observer { search ->
            teacherViewModel.getClassStudent(token, search).observe(viewLifecycleOwner, Observer { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        adapter?.submitList(resource.data)
                        showLoadingDialog(false)
                    }
                    Status.ERROR -> {
                        showLoadingDialog(false)
                    }
                    Status.LOADING -> {
                        showLoadingDialog()
                        Timber.i("loading...")
                    }
                }
            })
        })
        teacherViewModel.isSuccess.observe(viewLifecycleOwner, Observer {
            if (it) {
                snackBar?.setText(getString(R.string.upload_msg_success))?.show()
            } else {
                snackBar?.setText(getString(R.string.upload_msg_starting))?.show()
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == FILE_CHOOSER_RESULT) {
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

    //region Permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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
