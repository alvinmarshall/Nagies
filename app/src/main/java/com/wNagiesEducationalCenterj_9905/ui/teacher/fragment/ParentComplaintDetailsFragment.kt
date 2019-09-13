package com.wNagiesEducationalCenterj_9905.ui.teacher.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.ui.teacher.viewmodel.TeacherViewModel
import kotlinx.android.synthetic.main.fragment_parent_complaint_details.*

class ParentComplaintDetailsFragment : BaseFragment() {
    private lateinit var teacherViewModel: TeacherViewModel
    private var complaintId: Int? = null
    private var itemTitle: TextView? = null
    private var itemContent: TextView? = null
    private var itemDate: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parent_complaint_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemTitle = item_title
        itemContent = item_content
        itemDate = item_sender
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        complaintId = arguments?.let { ParentComplaintDetailsFragmentArgs.fromBundle(it).argTeacherComplaintId }
        configureViewModel()
    }

    private fun configureViewModel() {
        teacherViewModel = ViewModelProviders.of(this, viewModelFactory)[TeacherViewModel::class.java]
        complaintId?.let { teacherViewModel.getComplaintMessageById(it) }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        teacherViewModel.cachedComplaint.observe(viewLifecycleOwner, Observer { complaint ->
            val sender = "Guardian: ${complaint.guardianName}"
            val child = "Child ${complaint.studentName}"
            itemTitle?.text = sender
            itemDate?.text = child
            itemContent?.text = complaint.message
        })
    }


}
