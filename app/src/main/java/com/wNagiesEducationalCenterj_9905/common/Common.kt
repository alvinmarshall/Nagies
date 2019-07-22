package com.wNagiesEducationalCenterj_9905.common

import android.widget.TextView
import com.wNagiesEducationalCenterj_9905.R

fun showAnyView(
    view: Any?,
    message: String?,
    listener: Any?,
    visible: Boolean = false,
    Func: (Any?, String?, Any?, Boolean) -> Unit
) {
    Func(view, message, listener, visible)
}

fun showDataAvailableMessage(view: Any?, data: Any?,type:MessageType) {
    when(type){
        MessageType.MESSAGES ->{
            if (data == null) {
                (view as TextView).text = view.context.getString(R.string.label_no_messages_available)
                return
            }
            (view as TextView).text = view.context.getString(R.string.label_admin_message)
        }
        MessageType.FILES -> {
            if (data == null) {
                (view as TextView).text = view.context.getString(R.string.label_no_data_available)
                return
            }
            (view as TextView).text = view.context.getString(R.string.label_msg_available_files)
        }
    }
}