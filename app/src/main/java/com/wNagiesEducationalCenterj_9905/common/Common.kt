package com.wNagiesEducationalCenterj_9905.common

import android.view.View
import android.widget.TextView
import com.wNagiesEducationalCenterj_9905.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun showAnyView(
    view: Any?,
    message: String?,
    listener: Any?,
    visible: Boolean = false,
    Func: (Any?, String?, Any?, Boolean) -> Unit
) {
    Func(view, message, listener, visible)
}

fun showDataAvailableMessage(view: Any?, data: Any?, type: MessageType) {
    when (type) {
        MessageType.MESSAGES -> {
            if (data == null || (data as ArrayList<*>).isNullOrEmpty()) {
                (view as TextView).text = view.context.getString(R.string.label_no_messages_available)
                return
            }
            (view as TextView).text = view.context.getString(R.string.label_admin_message)
        }
        MessageType.FILES -> {
            if (data == null || (data as ArrayList<*>).isNullOrEmpty()) {
                (view as TextView).text = view.context.getString(R.string.label_no_data_available)
                return
            }
            (view as TextView).text = view.context.getString(R.string.label_msg_available_files)
        }
        MessageType.TEACHERS -> {
            if (data == null || (data as ArrayList<*>).isNullOrEmpty()) {
                (view as TextView).text = view.context.getString(R.string.label_msg_no_class_teachers)
                return
            }
            (view as TextView).text = ""
            view.visibility = View.GONE
        }
    }
}

fun getDifferenceInTime(time:String?):Int{
    val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    if (time == null) return 0
    return try {
        val dateTime = formatter.parse(time)
        val diff = Date(Date().time - dateTime.time)
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = diff
        val minute = calendar.get(Calendar.MINUTE)
        minute
    }catch (e:Exception){
        0
    }
}