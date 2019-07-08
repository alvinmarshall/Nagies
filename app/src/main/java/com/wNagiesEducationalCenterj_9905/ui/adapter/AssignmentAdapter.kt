package com.wNagiesEducationalCenterj_9905.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.data.db.Entities.AssignmentEntity
import kotlinx.android.synthetic.main.list_assignment.view.*
import java.io.File

class AssignmentAdapter : ListAdapter<AssignmentEntity, AssignmentVH>(AssignmentDiffUtil()) {
    private var itemCallback: ItemCallback<Pair<Int?, String?>>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentVH {
        return AssignmentVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_assignment, parent, false)
        )

    }

    override fun onBindViewHolder(holder: AssignmentVH, position: Int) {
        holder.bind(getItem(position), itemCallback)

    }

    fun setItemCallback(callback: ItemCallback<Pair<Int?, String?>>) {
        itemCallback = callback
    }
}

class AssignmentVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: AssignmentEntity?, itemCallback: ItemCallback<Pair<Int?, String?>>?) {
        itemView.tv_item_date.text = item?.reportDate
        itemView.tv_item_subject.text = item?.teacherEmail
        itemView.tv_item_name.text = if (item?.path != null) "downloaded" else "download"
        if (item?.path != null) {
            val file = File(item.path!!)
            if (file.exists()) {
                itemView.tv_item_download.visibility = View.GONE
                itemView.setOnClickListener {
                    itemCallback?.onClick(Pair(item.id, item.path))
                }
            } else {
                itemView.tv_item_download.visibility = View.VISIBLE
            }
        } else {
            itemView.tv_item_download.visibility = View.VISIBLE
        }
        itemView.tv_item_download.setOnClickListener {
            itemCallback?.onClick(Pair(item?.id, item?.reportFile))
        }

        itemView.setOnLongClickListener {
            itemCallback?.onHold(Pair(item?.id, item?.path))
            true
        }
    }
}


private class AssignmentDiffUtil : DiffUtil.ItemCallback<AssignmentEntity>() {
    override fun areItemsTheSame(oldItem: AssignmentEntity, newItem: AssignmentEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AssignmentEntity, newItem: AssignmentEntity): Boolean {
        return oldItem.teacherEmail == newItem.teacherEmail &&
                oldItem.reportFile == newItem.reportFile &&
                oldItem.studentName == newItem.reportDate &&
                oldItem.studentName == newItem.studentName
    }
}