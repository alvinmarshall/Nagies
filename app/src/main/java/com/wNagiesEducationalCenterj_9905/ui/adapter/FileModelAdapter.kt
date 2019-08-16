package com.wNagiesEducationalCenterj_9905.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.vo.IFileModel
import kotlinx.android.synthetic.main.list_assignment.view.*
import java.io.File

class FileModelAdapter : ListAdapter<IFileModel, FileModelVH>(FileModelDiffUtil()) {
    private var itemCallback: ItemCallback<Pair<Int?, String?>>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileModelVH {
        return FileModelVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_assignment, parent, false)
        )

    }

    override fun onBindViewHolder(holder: FileModelVH, position: Int) {
        holder.bind(getItem(position), itemCallback)

    }

    fun setItemCallback(callback: ItemCallback<Pair<Int?, String?>>) {
        itemCallback = callback
    }
}

class FileModelVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: IFileModel?, itemCallback: ItemCallback<Pair<Int?, String?>>?) {
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


private class FileModelDiffUtil : DiffUtil.ItemCallback<IFileModel>(){
    override fun areItemsTheSame(oldItem: IFileModel, newItem: IFileModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: IFileModel, newItem: IFileModel): Boolean {
        return oldItem.path == newItem.path
    }
}