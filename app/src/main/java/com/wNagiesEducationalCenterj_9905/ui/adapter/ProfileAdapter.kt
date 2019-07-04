package com.wNagiesEducationalCenterj_9905.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.vo.Profile
import kotlinx.android.synthetic.main.list_profile.view.*

class ProfileAdapter : RecyclerView.Adapter<ProfileVH>() {
    private var profileLabels: MutableList<Pair<Profile, String?>>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileVH {
        return ProfileVH(LayoutInflater.from(parent.context).inflate(R.layout.list_profile, parent, false))
    }

    override fun getItemCount(): Int {
        return profileLabels?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProfileVH, position: Int) {
        val label = profileLabels?.get(position)?.first
        val data = profileLabels?.get(position)?.second
        holder.bind(label, data)
    }

    fun setProfileData(profileInfo: MutableList<Pair<Profile, String?>>) {
        profileLabels = profileInfo
        notifyDataSetChanged()
    }
}

class ProfileVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(label: Profile?, data: String?) {
        label?.icon?.let { itemView.item_drawable.setImageResource(it) }
        itemView.item_label.text = label?.title?.trim()
        itemView.item_info.text = data?.trim()
    }

}