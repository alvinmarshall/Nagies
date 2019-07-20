package com.wNagiesEducationalCenterj_9905.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.api.request.ChangePasswordRequest
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.showAnyView
import com.wNagiesEducationalCenterj_9905.common.utils.InputValidationProvider
import com.wNagiesEducationalCenterj_9905.common.utils.NetworkStateUtils
import com.wNagiesEducationalCenterj_9905.ui.auth.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_change_password.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject

class ChangePasswordFragment : BaseFragment() {
    @Inject
    lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var inputValidationProvider: InputValidationProvider
    private var oldPass: String? = null
    private var newPass: String? = null
    private var confirmPass: String? = null
    private var loadingIndicator: ProgressBar? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureViewModel()
    }

    private fun configureViewModel() {
        authViewModel = ViewModelProviders.of(this, viewModelFactory)[AuthViewModel::class.java]
        authViewModel.isSuccess.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) {
                toast("Account Password changed Success")
                newPass?.let { authViewModel.updateAccountPassword(it) }
                showLoadingDialog(false)
                clearInput()
                return@Observer
            }
            showLoadingDialog(false)
            toast("Account Password Failed")
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingIndicator = progressBar
        loadingIndicator?.visibility = View.GONE
        btn_reset_password.onClick {
            prepareToChangePassword()
        }
    }

    private fun prepareToChangePassword() {
        if (!inputValidationProvider.isEditTextFilled(et_old_password, "field empty")) {
            return
        }
        if (!inputValidationProvider.isEditTextFilled(et_new_password, "field empty")) {
            return
        }
        if (!inputValidationProvider.isEditTextFilled(et_confirm_password, "field empty")) {
            return
        }
        if (!inputValidationProvider.isEditTextFilledMatches(
                et_new_password,
                et_confirm_password,
                "mismatch password"
            )
        ) {
            return
        }

        oldPass = et_old_password.text.toString()
        newPass = et_new_password.text.toString()
        confirmPass = et_confirm_password.text.toString()
        val changePasswordRequest = ChangePasswordRequest(oldPass, newPass, confirmPass)
        accountPasswordChange(changePasswordRequest)
    }

    private fun accountPasswordChange(request: ChangePasswordRequest) {
        if (NetworkStateUtils.isOnline(context!!)) {
            showLoadingDialog()
            authViewModel.changeAccountPassword(request)
        }
    }

    private fun clearInput() {
        et_old_password.text.clear()
        et_new_password.text.clear()
        et_confirm_password.text.clear()
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
