package com.wNagiesEducationalCenterj_9905.ui.parent.fragment


import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.common.MessageType
import com.wNagiesEducationalCenterj_9905.common.showAnyView
import com.wNagiesEducationalCenterj_9905.common.showDataAvailableMessage
import com.wNagiesEducationalCenterj_9905.ui.adapter.MessageAdapter
import com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel.StudentViewModel
import com.wNagiesEducationalCenterj_9905.viewmodel.SharedViewModel
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber

class DashboardFragment : BaseFragment() {
    private lateinit var studentViewModel: StudentViewModel
    private var loadingIndicator: ProgressBar? = null
    private var messageAdapter: MessageAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var shouldFetch: Boolean = false
    private lateinit var sharedViewModel: SharedViewModel
    private var snackBar: Snackbar? = null
    private var searchView: SearchView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun configureViewModel() {
        studentViewModel = ViewModelProviders.of(this, viewModelFactory)[StudentViewModel::class.java]
        studentViewModel.searchString.postValue("")
        getNetworkState()?.observe(viewLifecycleOwner, Observer {
            if (!it) {
                snackBar?.show()
                return@Observer
            }
            snackBar?.dismiss()
        })
        studentViewModel.getUserToken()
        studentViewModel.cachedToken.observe(viewLifecycleOwner, Observer {
            subscribeObserver(it)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingIndicator = progressBar
        loadingIndicator?.visibility = View.GONE
        recyclerView = recycler_view
        initRecyclerView()
        snackBar = Snackbar.make(root, getString(R.string.label_msg_offline), Snackbar.LENGTH_INDEFINITE)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
        searchView = menu.findItem(R.id.action_search).actionView as? SearchView
        searchView?.isSubmitButtonEnabled
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                studentViewModel.searchString.postValue(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                studentViewModel.searchString.postValue(newText)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initRecyclerView() {
        recyclerView?.hasFixedSize()
        recyclerView?.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        messageAdapter = MessageAdapter()
        messageAdapter?.setItemCallback(object : ItemCallback<Int> {
            override fun onHold(data: Int?) {
            }

            override fun onClick(data: Int?) {
                val action =
                    data?.let { DashboardFragmentDirections.actionDashboardFragmentToMessageDetailFragment(it) }
                activity?.let { Navigation.findNavController(it, R.id.fragment_socket).navigate(action!!) }
            }
        })
        recyclerView?.adapter = messageAdapter
    }

    private fun subscribeObserver(token: String?) {
        studentViewModel.searchString.observe(viewLifecycleOwner, Observer { search->
            token?.let {
                studentViewModel.getStudentMessages(it, shouldFetch,search).observe(viewLifecycleOwner, Observer { r ->
                    when (r.status) {
                        Status.SUCCESS -> {
                            showDataAvailableMessage(label_msg_title, r.data, MessageType.MESSAGES)
                            messageAdapter?.submitList(r.data)
                            showLoadingDialog(false)
                            Timber.i("message data size: ${r.data?.size}")
                        }
                        Status.ERROR -> {
                            showLoadingDialog(false)
                            showDataAvailableMessage(label_msg_title, r.data, MessageType.MESSAGES)
                            Timber.i(r.message)
                        }
                        Status.LOADING -> {
                            showLoadingDialog()
                        }
                    }
                })
            }
        })

        getFetchMessage().observe(viewLifecycleOwner, Observer {
            if (it) {
                Timber.i("notification received")
                shouldFetch = it
            }
        })

        activity?.let {
            sharedViewModel = ViewModelProviders.of(it)[SharedViewModel::class.java]
            sharedViewModel.fetchMessage.observe(it, Observer { fetch ->
                if (fetch) {
                    Timber.i("notification received from activity")
                    shouldFetch = fetch
                }
            })
        }
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

    override fun onStop() {
        super.onStop()
        sharedViewModel.fetchMessage.value = false
    }

}
