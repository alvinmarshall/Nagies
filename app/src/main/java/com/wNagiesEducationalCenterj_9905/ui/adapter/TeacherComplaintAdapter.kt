package com.wNagiesEducationalCenterj_9905.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.ComplaintAction
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.data.db.Entities.TeacherComplaintEntity
import kotlinx.android.synthetic.main.list_complaint.view.*

class TeacherComplaintAdapter : ListAdapter<TeacherComplaintEntity, TComplaintVH>(TComplaintDiff()) {
    private var itemCallback: ItemCallback<Pair<ComplaintAction, String?>>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TComplaintVH {
        return TComplaintVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_complaint, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TComplaintVH, position: Int) {
        holder.bind(getItem(position), itemCallback)
    }

    fun setItemCallBack(callback: ItemCallback<Pair<ComplaintAction, String?>>?) {
        itemCallback = callback
    }

}

class TComplaintVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: TeacherComplaintEntity?, itemCallback: ItemCallback<Pair<ComplaintAction, String?>>?) {
        val sender = "guardian: ${item?.guardianName}"
        val child = "child: ${item?.studentName}"
        val date = "date: ${item?.date}"
        itemView.tv_guardian_name.text = sender
        itemView.tv_student_name.text = child
        itemView.tv_msg_content.text = item?.message
        itemView.tv_msg_date.text = date
        itemView.btn_item_call.setOnClickListener { itemCallback?.onClick(Pair(ComplaintAction.CALL, item?.guardianContact)) }
        itemView.btn_item_message.setOnClickListener { itemCallback?.onClick(Pair(ComplaintAction.MESSAGE, item?.guardianContact)) }
        itemView.setOnClickListener { itemCallback?.onClick(Pair(ComplaintAction.DETAILS,item?.id.toString())) }
    }
}

private class TComplaintDiff : DiffUtil.ItemCallback<TeacherComplaintEntity>() {
    override fun areItemsTheSame(oldItem: TeacherComplaintEntity, newItem: TeacherComplaintEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TeacherComplaintEntity, newItem: TeacherComplaintEntity): Boolean {
        return oldItem.message == newItem.message
    }
}