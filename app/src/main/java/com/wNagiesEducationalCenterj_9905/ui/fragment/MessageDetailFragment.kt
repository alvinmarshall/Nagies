package com.wNagiesEducationalCenterj_9905.ui.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel.StudentViewModel
import kotlinx.android.synthetic.main.fragment_message_detail.*
/**
 * A simple [Fragment] subclass.
 *
 */
class MessageDetailFragment : BaseFragment() {

    private lateinit var studentViewModel: StudentViewModel
    private var messageId: Int? = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        messageId = arguments?.let { MessageDetailFragmentArgs.fromBundle(it).argsMessageId }
        configureViewModel()
        subscribeObserver()
    }

    private fun subscribeObserver() {
        studentViewModel.cachedMessage.observe(viewLifecycleOwner, Observer { msg ->
            val title = "class: ${msg.level}"
            val sender = "from: ${msg.sender}"
            item_title.text = title
            item_sender.text = sender
            item_content.text = msg.content
        })
    }

    private fun configureViewModel() {
        studentViewModel = ViewModelProviders.of(this, viewModelFactory)[StudentViewModel::class.java]
        messageId?.let { studentViewModel.getMessageById(it) }
    }

}
