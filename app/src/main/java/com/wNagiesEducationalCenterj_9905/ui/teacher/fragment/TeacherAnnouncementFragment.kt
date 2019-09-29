package com.wNagiesEducationalCenterj_9905.ui.teacher.fragment


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
import com.google.android.material.snackbar.Snackbar
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.*
import com.wNagiesEducationalCenterj_9905.ui.adapter.MessageAdapter
import com.wNagiesEducationalCenterj_9905.ui.teacher.viewmodel.TeacherViewModel
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.fragment_teacher_announcement.*
import timber.log.Timber

class TeacherAnnouncementFragment : BaseFragment() {
    private lateinit var teacherViewModel: TeacherViewModel
    private var loadingIndicator: ProgressBar? = null
    private var adapter: MessageAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var snackBar: Snackbar? = null
    private var searchView: SearchView? = null
    private var shouldFetch: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_announcement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingIndicator = progressBar
        loadingIndicator?.visibility = View.GONE
        recyclerView = recycler_view
        snackBar = Snackbar.make(root, getString(R.string.label_msg_offline), Snackbar.LENGTH_INDEFINITE)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val extra = arguments?.getBoolean(ANNOUNCEMENT_RECEIVE_EXTRA)
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
        recyclerView?.hasFixedSize()
        recyclerView?.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        adapter = MessageAdapter()
        adapter?.setItemCallback(object : ItemCallback<Int> {
            override fun onClick(data: Int?) {
                val action = data?.let {
                    TeacherAnnouncementFragmentDirections
                        .actionTeacherAnnouncementFragmentToAnnouncementDetailsFragment(it)
                }
                activity?.let { Navigation.findNavController(it, R.id.fragment_socket).navigate(action!!) }
            }

            override fun onHold(data: Int?) {
            }
        })
        recyclerView?.adapter = adapter
    }

    private fun configureViewModel() {
        teacherViewModel = ViewModelProviders.of(this, viewModelFactory)[TeacherViewModel::class.java]
        teacherViewModel.searchString.postValue("")
        getNetworkState()?.observe(viewLifecycleOwner, Observer {
            if (!it) {
                snackBar?.show()
                return@Observer
            }
            snackBar?.dismiss()
        })
        teacherViewModel.getUserToken()
        teacherViewModel.userToken.observe(viewLifecycleOwner, Observer { token ->
            subscribeObservers(token)
        })
    }

    private fun subscribeObservers(token: String) {
        teacherViewModel.searchString.observe(viewLifecycleOwner, Observer { search ->
            shouldFetch.observe(viewLifecycleOwner, Observer { fetch ->
                if (fetch){
                    preferenceProvider.setNotificationCallback(ANNOUNCEMENT_RECEIVE_EXTRA, false)
                }
                teacherViewModel.getAnnouncementMessage(token, fetch, search)
                    .observe(viewLifecycleOwner, Observer { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                showDataAvailableMessage(label_msg_title, resource.data, MessageType.MESSAGES)
                                adapter?.submitList(resource?.data)
                                showLoadingDialog(false)
                                Timber.i("data size: ${resource.data?.size}")
                            }
                            Status.ERROR -> {
                                showDataAvailableMessage(label_msg_title, resource.data, MessageType.MESSAGES)
                                showLoadingDialog(false)
                            }
                            Status.LOADING -> {
                                Timber.i("loading...")
                                showLoadingDialog()
                            }
                        }
                    })
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

    override fun onResume() {
        super.onResume()
        val perf = preferenceProvider.getNotificationCallback(ANNOUNCEMENT_RECEIVE_EXTRA)
        perf?.let {
            if (it) {
                shouldFetch.value = it
            }
        }
    }

//    override fun onPause() {
//        super.onPause()
//        preferenceProvider.setNotificationCallback(ANNOUNCEMENT_RECEIVE_EXTRA, false)
//    }


}
