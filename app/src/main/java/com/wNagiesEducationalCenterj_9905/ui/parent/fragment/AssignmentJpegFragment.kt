package com.wNagiesEducationalCenterj_9905.ui.parent.fragment


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.common.utils.FileTypeUtils
import com.wNagiesEducationalCenterj_9905.ui.adapter.AssignmentAdapter
import com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel.StudentViewModel
import com.wNagiesEducationalCenterj_9905.vo.DownloadRequest
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.fragment_assignment_jpeg.*
import timber.log.Timber
import java.io.File

class AssignmentJpegFragment : BaseFragment() {
    private lateinit var studentViewModel: StudentViewModel
    private var recyclerView: RecyclerView? = null
    private var assignmentAdapter: AssignmentAdapter? = null
    private var alertDialog: AlertDialog.Builder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_assignment_jpeg, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = recycler_view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        configureViewModel()
    }

    private fun configureViewModel() {
        studentViewModel = ViewModelProviders.of(this, viewModelFactory)[StudentViewModel::class.java]
        studentViewModel.getUserToken()
        studentViewModel.cachedToken.observe(viewLifecycleOwner, Observer { token ->
            studentViewModel.getStudentAssignmentImage(token).observe(viewLifecycleOwner, Observer { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Timber.i("assignment image data :${resource.data?.size}")
                        assignmentAdapter?.submitList(resource?.data)
                    }
                    Status.ERROR -> {
                        Timber.i(resource.message)
                    }
                    Status.LOADING -> {
                        Timber.i("loading...")
                    }
                }

            })

        })
    }

    private fun initRecyclerView() {
        recyclerView?.hasFixedSize()
        recyclerView?.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        assignmentAdapter = AssignmentAdapter()
        assignmentAdapter?.setItemCallback(object : ItemCallback<Pair<Int?, String?>> {
            override fun onClick(data: Pair<Int?, String?>?) {
                studentViewModel.downloadFilesFromServer(DownloadRequest(data?.second), data?.first)
                if (data?.second != null) {
                    val file = File(data.second!!)
                    if (file.exists()) {
                        val openIntent = Intent(Intent.ACTION_VIEW)
                        openIntent.data = Uri.fromFile(file)
                        openIntent.setDataAndType(Uri.fromFile(file), FileTypeUtils.getType(file.absolutePath))
                        Timber.i("type :${FileTypeUtils.getType(file.absolutePath)}")
                        val open = Intent.createChooser(openIntent, "choose an application to open file")
                        startActivity(open)
                    }
                }
            }

            override fun onHold(data: Pair<Int?, String?>?) {
                showDeleteDialog(data)
            }
        })
        recyclerView?.adapter = assignmentAdapter
    }

    private fun showDeleteDialog(data: Pair<Int?, String?>?) {
        alertDialog = context?.let { AlertDialog.Builder(it) }
        alertDialog?.setTitle("Delete Alert")
        alertDialog?.setMessage("Do you want to delete this file ?")
        alertDialog?.setPositiveButton("yes") { dialog, _ ->
            studentViewModel.deleteAssignmentById(data?.first, data?.second)
            dialog.dismiss()
        }
        alertDialog?.setNegativeButton("cancel", null)
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }


}
