package com.wNagiesEducationalCenterj_9905.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.data.db.Entities.ReportEntity
import kotlinx.android.synthetic.main.list_assignment.view.*
import java.io.File

class ReportAdapter : ListAdapter<ReportEntity, ReportVH>(ReportDiffUtil()) {

    private var itemCallback: ItemCallback<Pair<Int?, String?>>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportVH {
        return ReportVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_assignment, parent, false)
        )

    }

    override fun onBindViewHolder(holder: ReportVH, position: Int) {
        holder.bind(getItem(position),itemCallback)
    }


    fun setItemCallback(callback: ItemCallback<Pair<Int?, String?>>) {
        itemCallback = callback
    }
}

class ReportVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: ReportEntity?, itemCallback: ItemCallback<Pair<Int?, String?>>?) {
        itemView.tv_item_date.text = item?.date
        itemView.tv_item_subject.text = item?.teacherEmail
        itemView.tv_item_name.text = if (item?.path != null) "downloaded" else "download"
        when (item?.format){
            "pdf" -> {itemView.img_item_logo.setImageResource(R.drawable.ic_picture_as_pdf_black_24dp)}
            "image" -> {itemView.img_item_logo.setImageResource(R.drawable.ic_image_black_24dp)}
        }

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
            itemCallback?.onClick(Pair(item?.id, item?.fileUrl))
        }

        itemView.setOnLongClickListener {
            itemCallback?.onHold(Pair(item?.id, item?.path))
            true
        }
    }
}


private class ReportDiffUtil : DiffUtil.ItemCallback<ReportEntity>() {
    override fun areItemsTheSame(oldItem: ReportEntity, newItem: ReportEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ReportEntity, newItem: ReportEntity): Boolean {
        return oldItem.teacherEmail == newItem.teacherEmail &&
                oldItem.fileUrl == newItem.fileUrl &&
                oldItem.studentName == newItem.date &&
                oldItem.date == newItem.date
    }
}