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
import com.wNagiesEducationalCenterj_9905.vo.IFileModel
import kotlinx.android.synthetic.main.list_circular.view.*
import java.io.File

class TimetableAdapter : ListAdapter<IFileModel, TimetableVH>(TimetableDiff()) {
    private var itemCallback: ItemCallback<Triple<ViewFilesAction, Int?, String?>>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableVH {
        return TimetableVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_circular,
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: TimetableVH, position: Int) {
        holder.bind(getItem(position), itemCallback)
    }


    fun setCallBack(callback: ItemCallback<Triple<ViewFilesAction, Int?, String?>>?) {
        itemCallback = callback
    }

}

class TimetableVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
        item: IFileModel?,
        itemCallback: ItemCallback<Triple<ViewFilesAction, Int?, String?>>?
    ) {
        if (item?.path != null) {
            val file = File(item.path!!)
            if (file.exists()) {
                itemView.fab_download.hide()
                itemView.setOnClickListener {
                    itemCallback?.onClick(Triple(ViewFilesAction.VIEW, item.id, item.path))
                }
            } else {
                itemView.fab_download.show()
            }
        } else {
            itemView.fab_download.show()
        }
        GlideApp.with(itemView.context)
            .load(item?.fileUrl)
            .placeholder(R.drawable.notice_board)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(itemView.item_image)


        itemView.fab_download.setOnClickListener {
            itemCallback?.onClick(Triple(ViewFilesAction.DOWNLOAD, item?.id, item?.fileUrl))
        }
        itemView.setOnLongClickListener {
            itemCallback?.onHold(Triple(ViewFilesAction.DELETE, item?.id, item?.path))
            true
        }
    }
}

class TimetableDiff : DiffUtil.ItemCallback<IFileModel>() {
    override fun areContentsTheSame(oldItem: IFileModel, newItem: IFileModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areItemsTheSame(oldItem: IFileModel, newItem: IFileModel): Boolean {
        return oldItem.id == newItem.id && oldItem.path == newItem.path
    }
}