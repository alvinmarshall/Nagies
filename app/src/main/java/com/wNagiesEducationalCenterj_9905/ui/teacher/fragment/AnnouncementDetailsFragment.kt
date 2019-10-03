package com.wNagiesEducationalCenterj_9905.ui.teacher.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.ui.teacher.viewmodel.TeacherViewModel
import kotlinx.android.synthetic.main.fragment_announcement_details.*
import org.jetbrains.anko.support.v4.toast

class AnnouncementDetailsFragment : BaseFragment() {
    private lateinit var teacherViewModel: TeacherViewModel
    private var announcementId: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_announcement_details, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        announcementId = arguments?.let { AnnouncementDetailsFragmentArgs.fromBundle(it).argsAnnouncementId }
        configureViewModel()
    }

    private fun configureViewModel() {
        teacherViewModel = ViewModelProviders.of(this, viewModelFactory)[TeacherViewModel::class.java]
        announcementId?.let {  teacherViewModel.getAnnouncementById(it) }
        subscribeObservers()
    }

    private fun subscribeObservers() {

        teacherViewModel.cachedAnnouncement.observe(viewLifecycleOwner, Observer { msg ->
            val title = "From: ${msg.level}"
            val sender = "Name: ${msg.sender}"
            item_title.text = title
            item_sender.text = sender
            item_content.text = msg.content
        })
    }


}
