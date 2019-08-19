package com.wNagiesEducationalCenterj_9905.ui.parent.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel.StudentViewModel
import kotlinx.android.synthetic.main.fragment_announcement_details.*


class StudentAnnouncementDetailsFragment : BaseFragment() {
    private var announcementId:Int? = 0
    private lateinit var studentViewModel: StudentViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_announcement_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        announcementId = arguments?.let { StudentAnnouncementDetailsFragmentArgs.fromBundle(it).argsAnnouncementId }
        configureViewModel()
    }

    private fun configureViewModel() {
        studentViewModel = ViewModelProviders.of(this, viewModelFactory)[StudentViewModel::class.java]
        announcementId?.let {  studentViewModel.getAnnouncementById(it) }
        subscribeObservers()
    }

    private fun subscribeObservers() {

        studentViewModel.cachedAnnouncement.observe(viewLifecycleOwner, Observer { msg ->
            val title = "from: ${msg.level}"
            val sender = "name: ${msg.sender}"
            item_title.text = title
            item_sender.text = sender
            item_content.text = msg.content
        })
    }


}
