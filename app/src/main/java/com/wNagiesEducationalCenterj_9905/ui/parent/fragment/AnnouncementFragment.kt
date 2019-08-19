package com.wNagiesEducationalCenterj_9905.ui.parent.fragment


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
import com.google.android.material.snackbar.Snackbar
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.common.showAnyView
import com.wNagiesEducationalCenterj_9905.ui.adapter.MessageAdapter
import com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel.StudentViewModel
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.fragment_announcement.*
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber


class AnnouncementFragment : BaseFragment() {
    private lateinit var studentViewModel: StudentViewModel
    private var adapter: MessageAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var snackBar: Snackbar? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_announcement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = recycler_view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        configureViewModel()
    }

    private fun initRecyclerView() {
        recyclerView?.hasFixedSize()
        recyclerView?.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        adapter = MessageAdapter()
        adapter?.setItemCallback(object : ItemCallback<Int> {
            override fun onClick(data: Int?) {
                val action = data?.let {
                    AnnouncementFragmentDirections
                        .actionAnnouncementFragmentToStudentAnnouncementDetailsFragment(it)
                }
                activity?.let { Navigation.findNavController(it, R.id.fragment_socket).navigate(action!!) }
            }

            override fun onHold(data: Int?) {
            }

        })
        recyclerView?.adapter = adapter
    }

    private fun configureViewModel() {
        studentViewModel = ViewModelProviders.of(this, viewModelFactory)[StudentViewModel::class.java]
        getNetworkState()?.observe(viewLifecycleOwner, Observer {
            if (!it) {
                snackBar?.show()
                return@Observer
            }
            snackBar?.dismiss()
        })
        studentViewModel.getUserToken()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        studentViewModel.cachedToken.observe(viewLifecycleOwner, Observer { token ->
            studentViewModel.getAnnouncementMessage(token).observe(viewLifecycleOwner, Observer { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        showLoadingDialog(false)
                        adapter?.submitList(resource?.data)
                        Timber.i("data size: ${resource.data?.size}")
                    }
                    Status.ERROR -> {
                        showLoadingDialog(false)
                        toast("${resource.message}")
                    }
                    Status.LOADING -> {
                        Timber.i("loading...")
                        showLoadingDialog()
                    }
                }
            })
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
