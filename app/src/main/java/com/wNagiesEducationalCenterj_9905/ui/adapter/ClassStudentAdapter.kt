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
import com.wNagiesEducationalCenterj_9905.data.db.Entities.ClassStudentEntity
import kotlinx.android.synthetic.main.list_students.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class ClassStudentAdapter : ListAdapter<ClassStudentEntity, ClassStudentVH>(ClassStudentDiff()) {

    private var itemCallback: ItemCallback<ClassStudentEntity>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassStudentVH {
        return ClassStudentVH(LayoutInflater.from(parent.context).inflate(R.layout.list_students, parent, false))
    }

    override fun onBindViewHolder(holder: ClassStudentVH, position: Int) {
        val data = getItem(position)
        holder.bind(data, itemCallback)

    }

    fun setItemCallback(callback: ItemCallback<ClassStudentEntity>) {
        itemCallback = callback
    }
}


class ClassStudentVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
        data: ClassStudentEntity?,
        itemCallback: ItemCallback<ClassStudentEntity>?
    ) {
        val ref = "RefNo: ${data?.studentNo}"
        val name = "Name: ${data?.studentName}"
        val gender = "Gender: ${data?.gender}"
        val index = "IndexNo: ${data?.indexNo}"
        itemView.item_ref.text = ref
        itemView.item_name.text = name
        itemView.item_gender.text = gender
        itemView.item_index.text = index
        GlideApp.with(itemView.context).load(data?.imageUrl)
            .centerCrop()
            .circleCrop()
            .placeholder(R.drawable.default_user_avatar)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(itemView.item_img)

        itemView.btn_upload_report.onClick {
            itemCallback?.onClick(data)
        }


    }


}

class ClassStudentDiff : DiffUtil.ItemCallback<ClassStudentEntity>() {
    override fun areItemsTheSame(oldItem: ClassStudentEntity, newItem: ClassStudentEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ClassStudentEntity, newItem: ClassStudentEntity): Boolean {
        return oldItem.studentName == newItem.studentName &&
                oldItem.imageUrl == newItem.imageUrl
    }
}