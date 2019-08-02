package com.wNagiesEducationalCenterj_9905.ui.parent.fragment


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.ClassTeacherAction
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.ui.adapter.ClassTeacherAdapter
import com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel.StudentViewModel
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.fragment_class_teacher.*
import timber.log.Timber

class ClassTeacherFragment : BaseFragment() {
    private lateinit var studentViewModel: StudentViewModel
    private var adapter:ClassTeacherAdapter? = null
    private var recyclerView:RecyclerView? = null
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

    private fun initRecyclerView() {
        recyclerView?.hasFixedSize()
        recyclerView?.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        adapter = ClassTeacherAdapter()
        adapter?.setItemCallBack(object:ItemCallback<Pair<ClassTeacherAction,String?>>{
            override fun onClick(data: Pair<ClassTeacherAction, String?>?) {
                when(data?.first){
                    ClassTeacherAction.CALL -> {callTeacher(data.second)}
                    ClassTeacherAction.MESSAGE -> {sendSMSToTeacher(data.second)}
                }
            }

            override fun onHold(data: Pair<ClassTeacherAction, String?>?) {
            }

        })
        recyclerView?.adapter = adapter
    }

    private fun sendSMSToTeacher(contact: String?) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",contact,null)))
    }

    private fun callTeacher(contact: String?) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$contact")))
    }

    private fun configureViewModel() {
        studentViewModel = ViewModelProviders.of(this, viewModelFactory)[StudentViewModel::class.java]
        studentViewModel.getUserToken()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        studentViewModel.cachedToken.observe(viewLifecycleOwner, Observer {token->
            studentViewModel.getClassTeacher(token).observe(viewLifecycleOwner, Observer {resource->
                when(resource.status){
                    Status.SUCCESS -> {
                        Timber.i("fetch data size ${resource.data?.size}")
                        adapter?.submitList(resource.data)
                    }
                    Status.ERROR -> {Timber.i(resource.message)}
                    Status.LOADING -> {Timber.i("loading...")}
                }
            })

        })
    }


}
