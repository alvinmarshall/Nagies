package com.wNagiesEducationalCenterj_9905.common.utils

import android.content.Context
import android.graphics.drawable.Drawable
import com.wNagiesEducationalCenterj_9905.R

object ProfileLabel {
    fun getIconDrawables(context: Context): ArrayList<Drawable?> {
        return arrayListOf(
            context.applicationContext.getDrawable(R.drawable.ic_list_black_24dp),
            context.applicationContext.getDrawable(R.drawable.ic_person_black_24dp),
            context.applicationContext.getDrawable(R.drawable.ic_event_black_24dp),
            context.applicationContext.getDrawable(R.drawable.ic_wc_black_24dp),
            context.applicationContext.getDrawable(R.drawable.ic_event_black_24dp),
            context.applicationContext.getDrawable(R.drawable.ic_person_black_24dp),
            context.applicationContext.getDrawable(R.drawable.ic_access_time_black_24dp),
            context.applicationContext.getDrawable(R.drawable.ic_school_black_24dp),
            context.applicationContext.getDrawable(R.drawable.ic_person_black_24dp),
            context.applicationContext.getDrawable(R.drawable.ic_call_black_24dp),
            context.applicationContext.getDrawable(R.drawable.ic_business_black_24dp),
            context.applicationContext.getDrawable(R.drawable.ic_account_circle_black_24dp)
        )

    }

    fun getLabels(): ArrayList<String> {
        return arrayListOf(
            "StudentNo", "Student Name", "Date of Birth",
            "Gender", "Admission Date", "Academic Year",
            "Term", "Class Name", "Parent Name",
            "Contact", "Site Name", "Username"
        )
    }
}