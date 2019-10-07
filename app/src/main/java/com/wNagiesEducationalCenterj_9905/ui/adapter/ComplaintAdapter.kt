package com.wNagiesEducationalCenterj_9905.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.data.db.Entities.ComplaintEntity
import kotlinx.android.synthetic.main.list_messages.view.*

class ComplaintAdapter : ListAdapter<ComplaintEntity, ComplaintVH>(DiffUtil()) {
    private var itemCallback: ItemCallback<Int>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintVH {
        return ComplaintVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_messages, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ComplaintVH, position: Int) {
        holder.bind(getItem(position), itemCallback)
    }

    fun setItemCallback(callback: ItemCallback<Int>?) {
        itemCallback = callback
    }


}

class ComplaintVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
        item: ComplaintEntity?,
        itemCallback: ItemCallback<Int>?
    ) {
        val sender = "Guardian: ${item?.guardianName}"
        val date = "Date: ${item?.date}"
        val content = "Content: ${item?.message}"
        val teacher = "Teacher: ${item?.teacherName}"
        itemView.tv_msg_content.text = content
        itemView.tv_msg_date.text = date
        itemView.tv_msg_level.text = teacher
        itemView.tv_msg_sender.text = sender
        itemView.setOnClickListener {
            itemCallback?.onClick(item?.id)
        }
        itemView.setOnLongClickListener {
            itemCallback?.onHold(item?.uid)
            true
        }

    }
}

private class DiffUtil : DiffUtil.ItemCallback<ComplaintEntity>() {
    override fun areItemsTheSame(oldItem: ComplaintEntity, newItem: ComplaintEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ComplaintEntity, newItem: ComplaintEntity): Boolean {
        return oldItem.message == newItem.message &&
                oldItem.level == newItem.level
    }
}