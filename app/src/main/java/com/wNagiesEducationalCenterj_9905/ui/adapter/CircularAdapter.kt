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
import com.wNagiesEducationalCenterj_9905.common.utils.ServerPathUtil
import com.wNagiesEducationalCenterj_9905.data.db.Entities.CircularEntity
import kotlinx.android.synthetic.main.list_circular.view.*
import java.io.File

class CircularAdapter : PagerAdapter() {
    private var circularEntityList: List<CircularEntity>? = null
    private var itemCallback: ItemCallback<Triple<CircularAction, Int?, String?>>? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.list_circular, container, false)
        val data = circularEntityList?.get(position)

        if (data?.filePath != null) {
            val file = File(data.filePath!!)
            if (file.exists()) {
                view.fab_download.hide()
                view.setOnClickListener {
                    itemCallback?.onClick(Triple(CircularAction.VIEW, data.id, data.filePath))
                }
            }else{
                view.fab_download.show()
            }
        }else{
            view.fab_download.show()
        }
        view.fab_download.setOnClickListener {
            itemCallback?.onClick(Triple(CircularAction.DOWNLOAD, data?.id, ServerPathUtil.setCorrectPath(data?.path)))
        }

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

    override fun getCount() = circularEntityList?.size ?: 0

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun submitList(listEntity: List<CircularEntity>?) {
        circularEntityList = listEntity
        notifyDataSetChanged()
    }

    fun setCallBack(callback: ItemCallback<Triple<CircularAction, Int?, String?>>?) {
        itemCallback = callback
    }

}