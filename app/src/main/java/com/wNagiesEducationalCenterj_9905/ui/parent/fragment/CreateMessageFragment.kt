package com.wNagiesEducationalCenterj_9905.ui.parent.fragment


import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ProgressBar
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.api.request.ParentComplaintRequest
import com.wNagiesEducationalCenterj_9905.base.BaseFragment
import com.wNagiesEducationalCenterj_9905.common.utils.NetworkStateUtils
import com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel.StudentViewModel
import kotlinx.android.synthetic.main.fragment_create_message.*
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 *
 */
class CreateMessageFragment : BaseFragment() {

    private lateinit var studentViewModel: StudentViewModel
    private var messageContent: EditText? = null
    private var snackBar: Snackbar? = null
    private var complaintRequest: ParentComplaintRequest? = null
    private var loadingIndicator: ProgressBar? = null
    private var isBusy: Boolean = false
    private var dialog: AlertDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageContent = et_message_content
        loadingIndicator = progressBar
        showLoadingDialog(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureViewModel()
    }

    private fun configureViewModel() {
        studentViewModel = ViewModelProviders.of(this, viewModelFactory)[StudentViewModel::class.java]
        studentViewModel.isSuccess.observe(viewLifecycleOwner, Observer {
            isBusy = when (it) {
                true -> {
                    showLoadingDialog(false)
                    showMessage("message sent")
                    clearMessage()
                    false
                }
                false -> {
                    showLoadingDialog(false)
                    showMessage("sending message failed")
                    false
                }
            }
        })
        studentViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            showLoadingDialog(false)
            connectionErrorDialog(it)
            isBusy = false
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_send_message, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send -> {
                if (!isBusy) {
                    preparingToSendComplaint()
                }
            }
            R.id.action_close -> {
                showCancelAlertDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun clearMessage() {
        messageContent?.text = null
    }

    private fun preparingToSendComplaint() {
        val isConnected = context?.let { NetworkStateUtils.isOnline(it) }
        isConnected?.let {
            when (it) {
                true -> {
                    sendComplaint()
                }
                false -> {
                    showMessage("No internet connection...")
                }
            }
        }
    }

    private fun sendComplaint() {
        Timber.i("internet access...")
        val content: String? = messageContent?.text.toString()
        if (content.equals("") || content == null) {
            showMessage("message can't be empty")
            return
        }
        complaintRequest = ParentComplaintRequest(content)
        complaintRequest?.let {
            isBusy = true
            showLoadingDialog()
            studentViewModel.sendComplaint(it)
        }
    }

    private fun showMessage(message: String) {
        snackBar = Snackbar.make(root, message, Snackbar.LENGTH_LONG)
        snackBar?.show()
        Timber.i(message)
    }

    private fun connectionErrorDialog(@StringRes error_message: Int?) {
        snackBar = error_message?.let { Snackbar.make(root, it, Snackbar.LENGTH_LONG) }
        snackBar?.show()
    }

    private fun showLoadingDialog(show: Boolean = true) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun showCancelAlertDialog() {
        dialog = context?.let { AlertDialog.Builder(it) }
        dialog?.setTitle("Cancel Alert")
        dialog?.setMessage("Do you want to clear message ?")
        dialog?.setPositiveButton("yes") { dialog, _ ->
            clearMessage()
            dialog.dismiss()
        }
        dialog?.setNegativeButton("cancel", null)
        dialog?.setCancelable(false)
        dialog?.show()
    }


}
