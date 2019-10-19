package com.wNagiesEducationalCenterj_9905.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.GlideApp
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.common.ViewFilesAction
import com.wNagiesEducationalCenterj_9905.data.db.Entities.CircularEntity
import kotlinx.android.synthetic.main.list_circular.view.*
import java.io.File

class CircularAdapter : ListAdapter<CircularEntity, CircularViewHolder>(CircularDiff()) {
    private var itemCallback: ItemCallback<Triple<ViewFilesAction, Int?, String?>>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircularViewHolder {
        return CircularViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_circular, parent, false))

    }

    override fun onBindViewHolder(holder: CircularViewHolder, position: Int) {
        holder.bind(getItem(position), itemCallback)
    }


    fun setCallBack(callback: ItemCallback<Triple<ViewFilesAction, Int?, String?>>?) {
        itemCallback = callback
    }
}

class CircularViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
        data: CircularEntity?,
        itemCallback: ItemCallback<Triple<ViewFilesAction, Int?, String?>>?
    ) {
        if (data?.path != null) {
            val file = File(data.path!!)
            if (file.exists()) {
                itemView.fab_download.hide()
                itemView.setOnClickListener {
                    itemCallback?.onClick(Triple(ViewFilesAction.VIEW, data.id, data.path))
                }
            } else {
                itemView.fab_download.show()
            }
        } else {
            itemView.fab_download.show()
        }
        GlideApp.with(itemView.context)
            .load(data?.fileUrl)
            .placeholder(R.drawable.notice_board)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(itemView.item_image)


        itemView.fab_download.setOnClickListener {
            itemCallback?.onClick(Triple(ViewFilesAction.DOWNLOAD, data?.id, data?.fileUrl))
        }
        itemView.setOnLongClickListener {
            itemCallback?.onHold(Triple(ViewFilesAction.DELETE, data?.id, data?.path))
            true
        }
    }
}

class CircularDiff : DiffUtil.ItemCallback<CircularEntity>() {
    override fun areItemsTheSame(oldItem: CircularEntity, newItem: CircularEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CircularEntity, newItem: CircularEntity): Boolean {
        return oldItem.id == newItem.id && oldItem.path == newItem.path
    }
}
