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
    private var itemCallback:ItemCallback<Int>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintVH {
        return ComplaintVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_messages, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ComplaintVH, position: Int) {
        holder.bind(getItem(position),itemCallback)
    }

    fun setItemCallbak(callback: ItemCallback<Int>?){
        itemCallback = callback
    }

}

class ComplaintVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
        item: ComplaintEntity?,
        itemCallback: ItemCallback<Int>?
    ) {
        itemView.tv_msg_content.text = item?.content
        itemView.tv_msg_date.text = item?.date
        val sender = "sender: you"
        itemView.tv_msg_sender.text = sender
        itemView.setOnClickListener {
            itemCallback?.onClick(item?.id)
        }

    }
}

private class DiffUtil : DiffUtil.ItemCallback<ComplaintEntity>() {
    override fun areItemsTheSame(oldItem: ComplaintEntity, newItem: ComplaintEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ComplaintEntity, newItem: ComplaintEntity): Boolean {
        return oldItem.content == newItem.content &&
                oldItem.date == newItem.content &&
                oldItem.token == newItem.token
    }
}