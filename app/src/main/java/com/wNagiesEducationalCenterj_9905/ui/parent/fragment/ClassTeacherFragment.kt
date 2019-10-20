package com.wNagiesEducationalCenterj_9905.ui.parent.fragment


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.*
import com.wNagiesEducationalCenterj_9905.ui.adapter.ClassTeacherAdapter
import com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel.StudentViewModel
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.fragment_class_teacher.*
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber

class ClassTeacherFragment : BaseFragment() {
    private lateinit var studentViewModel: StudentViewModel
    private var adapter: ClassTeacherAdapter? = null
    private var recyclerView: RecyclerView? = null
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
        return inflater.inflate(R.layout.fragment_class_teacher, container, false)
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
        recyclerView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = ClassTeacherAdapter()
        adapter?.setItemCallBack(object : ItemCallback<Pair<ClassTeacherAction, String?>> {
            override fun onClick(data: Pair<ClassTeacherAction, String?>?) {
                when (data?.first) {
                    ClassTeacherAction.CALL -> {
                        callTeacher(data.second)
                    }
                    ClassTeacherAction.MESSAGE -> {
                        sendSMSToTeacher(data.second)
                    }
                }
            }

            override fun onHold(data: Pair<ClassTeacherAction, String?>?) {
            }

        })
        recyclerView?.adapter = adapter
    }

    private fun sendSMSToTeacher(contact: String?) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contact, null)))
    }

    private fun callTeacher(contact: String?) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$contact")))
    }

    private fun configureViewModel() {
        studentViewModel = ViewModelProvider(this, viewModelFactory)[StudentViewModel::class.java]
        studentViewModel.searchString.postValue("")
        studentViewModel.getUserToken()
        studentViewModel.cachedToken.observe(viewLifecycleOwner, Observer { token ->
            subscribeObservers(token)
        })
    }

    private fun subscribeObservers(token: String) {
        studentViewModel.searchString.observe(viewLifecycleOwner, Observer { search ->
            studentViewModel.getClassTeacher(token, search).observe(viewLifecycleOwner, Observer { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Timber.i("fetch data size ${resource.data?.size}")
                        showDataAvailableMessage(label_msg_title, resource.data, MessageType.TEACHERS)
                        adapter?.submitList(resource.data)
                        showLoadingDialog(false)
                    }
                    Status.ERROR -> {
                        Timber.i(resource.message)
                        showDataAvailableMessage(label_msg_title, resource.data, MessageType.TEACHERS)
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
