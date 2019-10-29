package com.wNagiesEducationalCenterj_9905.ui.teacher.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.GlideApp
import com.wNagiesEducationalCenterj_9905.ui.adapter.ProfileAdapter
import com.wNagiesEducationalCenterj_9905.ui.teacher.viewmodel.TeacherViewModel
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.fragment_teacher_profile.*
import kotlinx.coroutines.launch
import timber.log.Timber


class TeacherProfileFragment : BaseFragment() {
    private lateinit var teacherViewModel: TeacherViewModel
    private var profileAdapter: ProfileAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var loadingIndicator: ProgressBar? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_profile, container, false)
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
        recyclerView?.hasFixedSize()
        recyclerView?.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        recyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        profileAdapter = ProfileAdapter()
    }

    private fun configureViewModel() {
        teacherViewModel = ViewModelProvider(this, viewModelFactory)[TeacherViewModel::class.java]
        teacherViewModel.getUserToken()
        subscribeObservers()

    }

    private fun subscribeObservers() {
        teacherViewModel.userToken.observe(viewLifecycleOwner, Observer { token ->
            teacherViewModel.getTeacherProfile(token).observe(viewLifecycleOwner, Observer { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        teacherViewModel.setProfileLabels(resource.data)
                        teacherViewModel.cachedLabels.observe(viewLifecycleOwner, Observer { labels ->
                            profileAdapter?.setProfileData(labels)
                            recyclerView?.adapter = profileAdapter
                            setProfileImage(resource.data?.imageUrl)
                            showLoadingDialog(false)
                        })

                        Timber.i("data ${resource.data?.username}")
                    }
                    Status.ERROR -> {
                        showLoadingDialog(false)
                        Timber.i(resource.message)
                    }
                    Status.LOADING -> {
                        Timber.i("loading...")
                        showLoadingDialog()
                    }
                }
            })
        })
    }

    private fun setProfileImage(imageUrl: String?) = launch {
        if (imageUrl != null) {
            context?.let {
                GlideApp.with(it).load(imageUrl)
                    .placeholder(R.drawable.default_user_avatar)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(item_img)
            }
        }
    }

    private fun showLoadingDialog(show: Boolean = true) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}
