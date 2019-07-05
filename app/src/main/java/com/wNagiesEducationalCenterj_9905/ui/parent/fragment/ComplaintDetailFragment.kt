package com.wNagiesEducationalCenterj_9905.ui.parent.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel.StudentViewModel
import kotlinx.android.synthetic.main.fragment_complaint_detail.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ComplaintDetailFragment : BaseFragment() {
    private lateinit var studentViewModel: StudentViewModel
    private var complaintId:Int? = 0
    private var itemTitle:TextView? = null
    private var itemContent:TextView? = null
    private var itemDate:TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complaint_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemTitle = item_title
        itemContent = item_content
        itemDate = item_sender
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        complaintId = arguments?.let { ComplaintDetailFragmentArgs.fromBundle(it).argParentComplaintId }
        configureViewModel()
    }

    private fun configureViewModel() {
        studentViewModel = ViewModelProviders.of(this,viewModelFactory)[StudentViewModel::class.java]
        complaintId?.let { studentViewModel.getSavedParentComplaintById(it) }
        subscribeObserver()
    }

    private fun subscribeObserver() {
        studentViewModel.cachedSavedComplaintById.observe(viewLifecycleOwner, Observer {
            val sender = "sender: you"
            itemTitle?.text = sender
            itemDate?.text = it.date
            itemContent?.text = it.content
        })
    }

}
