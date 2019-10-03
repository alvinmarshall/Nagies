package com.wNagiesEducationalCenterj_9905.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.CircularAction
import com.wNagiesEducationalCenterj_9905.common.GlideApp
import com.wNagiesEducationalCenterj_9905.common.ItemCallback
import com.wNagiesEducationalCenterj_9905.vo.IFileModel
import kotlinx.android.synthetic.main.list_circular.view.*
import timber.log.Timber
import java.io.File

class FileviewPageAdapter : PagerAdapter() {
    private var fileEntity: List<IFileModel>? = null
    private var itemCallback: ItemCallback<Triple<CircularAction, Int?, String?>>? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.list_circular, container, false)
        val data = fileEntity?.get(position)
        view.fab_download.hide()

        GlideApp.with(container.context)
            .load(data?.fileUrl)
            .placeholder(R.drawable.notice_board)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(view.item_image)
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    fun submitList(listEntity: List<IFileModel>?) {
        fileEntity = listEntity
        notifyDataSetChanged()
    }


    override fun getCount(): Int = fileEntity?.size ?: 0

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}