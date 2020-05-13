package com.wNagiesEducationalCenterj_9905.ui.teacher.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.common.MessageType
import com.wNagiesEducationalCenterj_9905.common.showAnyView
import com.wNagiesEducationalCenterj_9905.common.showDataAvailableMessage
import com.wNagiesEducationalCenterj_9905.ui.adapter.MessageAdapter
import com.wNagiesEducationalCenterj_9905.ui.teacher.viewmodel.TeacherViewModel
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.fragment_send_message_teacher.*
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber

class SendMessageTeacherFragment : BaseFragment() {
    private lateinit var teacherViewModel: TeacherViewModel
    private var recyclerView: RecyclerView? = null
    private var loadingIndicator: ProgressBar? = null
    private var labelMessage: TextView? = null
    private var adapter: MessageAdapter? = null
    private var alertDialog: AlertDialog.Builder? = null
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
        alertDialog = context?.let { AlertDialog.Builder(it) }
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
                val action = data?.let {
                    SendMessageTeacherFragmentDirections.actionSendMessageTeacherFragmentToTeacherMessageDetailFragment(it)
                }
                activity?.let {
                    action?.let { action ->
                        Navigation.findNavController(it, R.id.fragment_socket).navigate(action)
                    }
                }
            }

            override fun onHold(data: Int?) {
                alertDialog?.setIcon(R.drawable.ic_delete_black_24dp)
                alertDialog?.setTitle("Delete Alert")
                alertDialog?.setMessage("Are you sure you want to delete this message?")
                alertDialog?.setPositiveButton("yes") { dialog, _ ->
                    dialog.dismiss()
                    deleteMessageFromServer(data)
                }
                alertDialog?.setNegativeButton("no", null)
                alertDialog?.show()

            }
        })
        recyclerView?.adapter = adapter
    }

    private fun deleteMessageFromServer(data: Int?) {
        getNetworkState()?.observe(viewLifecycleOwner, Observer { network ->
            if (network) {
                teacherViewModel.deleteMessage(data)
            }else{
                toast("no internet connection available")
            }
        })
    }

    private fun configureViewModel() {
        teacherViewModel = ViewModelProvider(this, viewModelFactory)[TeacherViewModel::class.java]
        subscribeObservers()
    }

    private fun subscribeObservers() {
        val token = preferenceProvider.getUserSessionData().token
        token?.let {
            teacherViewModel.isSuccess.observe(viewLifecycleOwner, Observer { success->
                teacherViewModel.getSentMessages(it,success).observe(viewLifecycleOwner, Observer { resources ->
                    when (resources.status) {
                        Status.SUCCESS -> {
                            showDataAvailableMessage(label_msg_title,resources.data, MessageType.MESSAGES)
                            adapter?.submitList(resources.data)
                            showLoadingDialog(false)
                            Timber.i("data size ${resources.data?.size}")
                        }
                        Status.ERROR -> {
                            showLoadingDialog(false)
                            showDataAvailableMessage(label_msg_title,resources.data, MessageType.MESSAGES)
                            resources.message?.let {
                                if (it.contains("Unable to resolve host")) {
                                    toast("No internet connection")
                                }
                            }
                            Timber.i(resources.message)
                        }
                        Status.LOADING -> {
                            showLoadingDialog()
                            Timber.i("loading...")
                        }
                    }
                })

            })
        }
        getNetworkState()?.observe(viewLifecycleOwner, Observer { network->
            teacherViewModel.isSuccess.value = network
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
