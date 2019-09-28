package com.wNagiesEducationalCenterj_9905.ui.teacher.fragment


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.*
import com.wNagiesEducationalCenterj_9905.ui.adapter.TeacherComplaintAdapter
import com.wNagiesEducationalCenterj_9905.ui.teacher.viewmodel.TeacherViewModel
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.fragment_parent_complaint.*
import timber.log.Timber
import javax.inject.Inject

class ParentComplaintFragment : BaseFragment() {
    @Inject
    lateinit var teacherViewModel: TeacherViewModel
    private var adapter: TeacherComplaintAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var loadingIndicator: ProgressBar? = null
    private var shouldFetch: MutableLiveData<Boolean> = MutableLiveData()
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
        val extra = arguments?.getBoolean(COMPLAINT_RECEIVE_EXTRA)
        extra?.let { result ->
            shouldFetch.value = result
        }
        initRecyclerView()
        configureViewModel()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
        searchView = menu.findItem(R.id.action_search).actionView as? SearchView
        searchView?.isSubmitButtonEnabled
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                teacherViewModel.searchString.postValue(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                teacherViewModel.searchString.postValue(newText)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initRecyclerView() {
        recyclerView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView?.hasFixedSize()
        adapter = TeacherComplaintAdapter()
        adapter?.setItemCallBack(object : ItemCallback<Pair<ComplaintAction, String?>> {
            override fun onClick(data: Pair<ComplaintAction, String?>?) {
                when (data?.first) {
                    ComplaintAction.DETAILS -> {
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

    private fun subscribeObservers(token: String) {
        teacherViewModel.searchString.observe(viewLifecycleOwner, Observer { search ->
            shouldFetch.observe(viewLifecycleOwner, Observer { fetch ->
                teacherViewModel.getComplaintMessage(token, fetch, search)
                    .observe(viewLifecycleOwner, Observer { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                showDataAvailableMessage(label_msg_title, resource.data, MessageType.MESSAGES)
                                adapter?.submitList(resource?.data)
                                showLoadingDialog(false)
                                Timber.i("data size: ${resource.data?.size}")
                            }
                            Status.ERROR -> {
                                showLoadingDialog(false)
                                showDataAvailableMessage(label_msg_title, resource.data, MessageType.MESSAGES)
                                Timber.i(resource?.message)
                            }
                            Status.LOADING -> {
                                Timber.i("loading")
                                showLoadingDialog()
                            }
                        }

                    })
            })

        })

    }

    private fun configureViewModel() {
        teacherViewModel = ViewModelProviders.of(this, viewModelFactory)[TeacherViewModel::class.java]
        teacherViewModel.searchString.postValue("")
        teacherViewModel.getUserToken()
        teacherViewModel.userToken.observe(viewLifecycleOwner, Observer { token ->
            subscribeObservers(token)
        })
    }

    override fun onResume() {
        super.onResume()
        val perf = preferenceProvider.getNotificationCallback(COMPLAINT_RECEIVE_EXTRA)
        perf?.let {
            if (it) {
                shouldFetch.value = it
            }
        }
    }

    override fun onPause() {
        super.onPause()
        preferenceProvider.setNotificationCallback(COMPLAINT_RECEIVE_EXTRA, false)

    }


}
