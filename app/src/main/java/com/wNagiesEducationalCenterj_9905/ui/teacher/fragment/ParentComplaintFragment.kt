package com.wNagiesEducationalCenterj_9905.ui.teacher.fragment


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.ComplaintAction
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.common.showAnyView
import com.wNagiesEducationalCenterj_9905.ui.adapter.TeacherComplaintAdapter
import com.wNagiesEducationalCenterj_9905.ui.teacher.viewmodel.TeacherViewModel
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.fragment_parent_complaint.*
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber
import javax.inject.Inject

class ParentComplaintFragment : BaseFragment() {
    @Inject
    lateinit var teacherViewModel: TeacherViewModel
    private var adapter: TeacherComplaintAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var loadingIndicator: ProgressBar? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parent_complaint, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = recycler_view
        loadingIndicator = progressBar
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        configureViewModel()

    }

    private fun initRecyclerView() {
        recyclerView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView?.hasFixedSize()
        adapter = TeacherComplaintAdapter()
        adapter?.setItemCallBack(object : ItemCallback<Pair<ComplaintAction, String?>> {
            override fun onClick(data: Pair<ComplaintAction, String?>?) {
                when (data?.first) {
                    ComplaintAction.DETAILS -> {
                        toast("${data.second}")
                        val action = data.second?.toInt()?.let { id ->
                            ParentComplaintFragmentDirections
                                .actionParentComplaintFragmentToParentComplaintDetailsFragment(id)
                        }
                        activity?.let { Navigation.findNavController(it, R.id.fragment_socket).navigate(action!!) }
                    }
                    ComplaintAction.CALL -> {
                        callParent(data.second)
                    }
                    ComplaintAction.MESSAGE -> {
                        sendSMSToParent(data.second)
                    }
                }
            }

            override fun onHold(data: Pair<ComplaintAction, String?>?) {
            }

        })
        recyclerView?.adapter = adapter
    }

    private fun sendSMSToParent(contact: String?) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contact, null)))
    }

    private fun callParent(contact: String?) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$contact")))
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

    private fun subscribeObservers() {
        teacherViewModel.userToken.observe(viewLifecycleOwner, Observer { token ->
            teacherViewModel.getComplaintMessage(token).observe(viewLifecycleOwner, Observer { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        showLoadingDialog(false)
                        adapter?.submitList(resource?.data)
                        Timber.i("data size: ${resource.data?.size}")
                    }
                    Status.ERROR -> {
                        showLoadingDialog(false)
                        Timber.i(resource?.message)
                    }
                    Status.LOADING -> {
                        Timber.i("loading")
                        showLoadingDialog()
                    }
                }

            })
        })
    }

    private fun configureViewModel() {
        teacherViewModel = ViewModelProviders.of(this, viewModelFactory)[TeacherViewModel::class.java]
        teacherViewModel.getUserToken()
        subscribeObservers()
    }


}
