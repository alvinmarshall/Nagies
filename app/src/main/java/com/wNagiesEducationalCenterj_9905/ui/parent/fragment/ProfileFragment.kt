package com.wNagiesEducationalCenterj_9905.ui.parent.fragment


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
import com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel.StudentViewModel
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.fragment_dashboard.recycler_view
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.launch
import timber.log.Timber


class ProfileFragment : BaseFragment() {
    private lateinit var studentViewModel: StudentViewModel
    private var profileAdapter: ProfileAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var loadingIndicator: ProgressBar? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = recycler_view
        loadingIndicator = progressBar
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView?.hasFixedSize()
        recyclerView?.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        recyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        profileAdapter = ProfileAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureViewModel()
    }

    private fun configureViewModel() {
        studentViewModel = ViewModelProvider(this, viewModelFactory)[StudentViewModel::class.java]
        studentViewModel.getUserToken()
        studentViewModel.cachedToken.observe(viewLifecycleOwner, Observer {
            subscribeObserver(it)
        })
    }

    private fun subscribeObserver(token: String?) {
        token?.let { t ->
            studentViewModel.getStudentProfile(t).observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        studentViewModel.setProfileLabels(it.data)
                        studentViewModel.cachedLabels.observe(viewLifecycleOwner, Observer { profile ->
                            profileAdapter?.setProfileData(profile)
                            recyclerView?.adapter = profileAdapter
                            setProfileImage(it.data?.imageUrl)
                            showLoadingDialog(false)
                        })
                        Timber.i("profile name: ${it.data?.studentName}")
                    }
                    Status.ERROR -> {
                        Timber.i(it.message)
                        showLoadingDialog(false)
                    }
                    Status.LOADING -> {
                        Timber.i("loading data...")
                        showLoadingDialog()
                    }
                }
            })
        }
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
