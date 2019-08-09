package com.wNagiesEducationalCenterj_9905.ui.parent.fragment


import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun configureViewModel() {
        studentViewModel = ViewModelProviders.of(this, viewModelFactory)[StudentViewModel::class.java]
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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
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
        token?.let {
            studentViewModel.getStudentMessages(it, shouldFetch).observe(viewLifecycleOwner, Observer { r ->
                when (r.status) {
                    Status.SUCCESS -> {
                        showLoadingDialog(false)
                        messageAdapter?.submitList(r.data)
                        Timber.i("message data size: ${r.data?.size}")
                    }
                    Status.ERROR -> {
                        showLoadingDialog(false)
                        Timber.i(r.message)
                    }
                    Status.LOADING -> {
                        showLoadingDialog()
                    }
                }
            })
        }
        getShouldFetch().observe(viewLifecycleOwner, Observer {
            if (it) {
                toast("notification received")
                shouldFetch = it
            }
        })

        activity?.let {
            sharedViewModel = ViewModelProviders.of(it)[SharedViewModel::class.java]
            sharedViewModel.fetchMessage.observe(it, Observer { fetch ->
                if (fetch) {
                    toast("notification received from activity")
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
