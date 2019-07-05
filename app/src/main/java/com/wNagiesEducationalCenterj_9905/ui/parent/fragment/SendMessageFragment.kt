package com.wNagiesEducationalCenterj_9905.ui.parent.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.ui.adapter.ComplaintAdapter
import com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel.StudentViewModel
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.fragment_send_message.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 *
 */
class SendMessageFragment : BaseFragment() {
    private lateinit var studentViewModel: StudentViewModel
    private var complaintAdapter: ComplaintAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var loadingIndicator: ProgressBar? = null
    private var labelMessage: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_send_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = recycler_view
        loadingIndicator = progressBar
        labelMessage = label_msg_title
        fab_create.onClick { navigateToCreateMessage() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        configureViewModel()
    }

    private fun initRecyclerView() {
        recyclerView?.hasFixedSize()
        recyclerView?.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        complaintAdapter = ComplaintAdapter()
        complaintAdapter?.setItemCallbak(object : ItemCallback<Int> {
            override fun onClick(data: Int?) {
                val action = data?.let {
                    SendMessageFragmentDirections.actionSendMessageFragmentToComplaintDetailFragment(it)
                }
                activity?.let {
                    action?.let { action ->
                        Navigation.findNavController(it, R.id.fragment_socket).navigate(action)
                    }
                }

            }

            override fun onHold(data: Int?) {
            }
        })
        recyclerView?.adapter = complaintAdapter
    }

    private fun configureViewModel() {
        studentViewModel = ViewModelProviders.of(this, viewModelFactory)[StudentViewModel::class.java]
        studentViewModel.getSavedParentComplaint()
        subscribeObserver()
    }

    private fun subscribeObserver() {
        studentViewModel.cachedSavedComplaint.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    showLoadingDialog(false)
                    complaintAdapter?.submitList(resource.data)
                }
                Status.ERROR -> {
                    showLoadingDialog(false)
                    showNoContentMessage(resource?.message)
                    Timber.i(resource.message)
                }
                Status.LOADING -> {
                    showLoadingDialog()
                    Timber.i("loading")
                }
            }
        })
    }

    private fun navigateToCreateMessage() {
        val action = SendMessageFragmentDirections.actionSendMessageFragmentToCreateMessageFragment()
        activity?.let { Navigation.findNavController(it, R.id.fragment_socket).navigate(action) }

    }

    private fun showLoadingDialog(show: Boolean = true) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun showNoContentMessage(msg: String?, show: Boolean = true) {
        if (show) {
            labelMessage?.visibility = View.VISIBLE
            labelMessage?.text = msg
        } else {
            labelMessage?.visibility = View.GONE
            labelMessage?.text = null
        }

    }

}
