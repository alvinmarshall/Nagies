package com.wNagiesEducationalCenterj_9905.ui.teacher.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.ui.teacher.viewmodel.TeacherViewModel
import kotlinx.android.synthetic.main.fragment_teacher_message_detail.*
import javax.inject.Inject

class TeacherMessageDetailFragment : BaseFragment() {
    private lateinit var teacherViewModel: TeacherViewModel
    private var messageId: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_message_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        messageId = arguments?.let { TeacherMessageDetailFragmentArgs.fromBundle(it).argsMessageId }
        configureViewModel()
        subscribeObserver()
    }

    private fun configureViewModel() {
        teacherViewModel = ViewModelProviders.of(this, viewModelFactory)[TeacherViewModel::class.java]
        messageId?.let { teacherViewModel.getMessageById(it) }
    }

    private fun subscribeObserver() {
        teacherViewModel.cachedMessage.observe(viewLifecycleOwner, Observer { msg ->
            val title = "class: ${msg.level}"
            val sender = "from: ${msg.sender}"
            item_title.text = title
            item_sender.text = sender
            item_content.text = msg.content
        })
    }

}
