package com.wNagiesEducationalCenterj_9905.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.ClassTeacherAction
import com.wNagiesEducationalCenterj_9905.common.GlideApp
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.data.db.Entities.StudentTeacherEntity
import kotlinx.android.synthetic.main.list_teachers.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class ClassTeacherAdapter : ListAdapter<StudentTeacherEntity, TeacherVH>(TeacherDiff()) {
    private var itemCallback: ItemCallback<Pair<ClassTeacherAction,String?>>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherVH {
        return TeacherVH(LayoutInflater.from(parent.context).inflate(R.layout.list_teachers, parent, false))
    }

    override fun onBindViewHolder(holder: TeacherVH, position: Int) {
        holder.bind(getItem(position), itemCallback)
    }

    fun setItemCallBack(callback: ItemCallback<Pair<ClassTeacherAction,String?>>?) {
        itemCallback = callback
    }
}

class TeacherVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: StudentTeacherEntity?, itemCallback: ItemCallback<Pair<ClassTeacherAction,String?>>?) {
        val name = "Name: ${item?.teacherName}"
        val gender = "Gender: ${item?.gender}"
        itemView.tv_item_name.text = name
        itemView.tv_item_gender.text = gender
        GlideApp.with(itemView.context)
            .load(item?.imageUrl)
            .centerCrop().placeholder(R.drawable.default_user_avatar)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(itemView.item_img)
        itemView.btn_item_call.onClick {
            itemCallback?.onClick(Pair(ClassTeacherAction.CALL,item?.contact))
        }
        itemView.btn_item_message.onClick {
            itemCallback?.onClick(Pair(ClassTeacherAction.MESSAGE,item?.contact))
        }
    }
}

private class TeacherDiff : DiffUtil.ItemCallback<StudentTeacherEntity>() {
    override fun areItemsTheSame(oldItem: StudentTeacherEntity, newItem: StudentTeacherEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: StudentTeacherEntity, newItem: StudentTeacherEntity): Boolean {
        return oldItem.teacherName == newItem.teacherName &&
                oldItem.contact == newItem.contact &&
                oldItem.imageUrl == newItem.imageUrl
    }
}