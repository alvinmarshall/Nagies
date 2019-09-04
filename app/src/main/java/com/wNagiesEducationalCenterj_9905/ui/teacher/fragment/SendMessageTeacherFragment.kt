package com.wNagiesEducationalCenterj_9905.ui.teacher.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.common.showAnyView
import com.wNagiesEducationalCenterj_9905.ui.adapter.MessageAdapter
import com.wNagiesEducationalCenterj_9905.ui.teacher.viewmodel.TeacherViewModel
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.fragment_send_message_teacher.*
import timber.log.Timber

class SendMessageTeacherFragment : BaseFragment() {
    private lateinit var teacherViewModel: TeacherViewModel
    private var recyclerView: RecyclerView? = null
    private var loadingIndicator: ProgressBar? = null
    private var labelMessage: TextView? = null
    private var adapter: MessageAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_message_teacher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = recycler_view
        loadingIndicator = progressBar
        labelMessage = label_msg_title
        fab_create.setOnClickListener {
            val action =
                SendMessageTeacherFragmentDirections.actionSendMessageTeacherFragmentToCreateTeacherMessageFragment()
            activity?.let { it1 -> Navigation.findNavController(it1, R.id.fragment_socket).navigate(action) }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        configureViewModel()
    }

    private fun initRecyclerView() {
        recyclerView?.hasFixedSize()
        recyclerView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = MessageAdapter()
        adapter?.setItemCallback(object : ItemCallback<Int> {
            override fun onClick(data: Int?) {
            }

            override fun onHold(data: Int?) {
            }
        })
        recyclerView?.adapter = adapter
    }

    private fun configureViewModel() {
        teacherViewModel = ViewModelProviders.of(this, viewModelFactory)[TeacherViewModel::class.java]
        teacherViewModel.getSentMessages()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        teacherViewModel.cachedSentMessage.observe(viewLifecycleOwner, Observer { resources ->
            when (resources.status) {
                Status.SUCCESS -> {
                    adapter?.submitList(resources.data)
                    showLoadingDialog(false)
                    Timber.i("data size ${resources.data?.size}")
                }
                Status.ERROR -> {
                    showLoadingDialog(false)
                    Timber.i(resources.message)
                }
                Status.LOADING -> {
                    showLoadingDialog()
                    Timber.i("loading...")
                }
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


}
