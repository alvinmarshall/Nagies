package com.wNagiesEducationalCenterj_9905.common.utils

import android.content.SharedPreferences
import com.wNagiesEducationalCenterj_9905.common.LOGIN_PREF
import com.wNagiesEducationalCenterj_9905.common.SELECTED_ROLE
import com.wNagiesEducationalCenterj_9905.common.USER_TOKEN
import javax.inject.Inject

class PreferenceProvider @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun setUserLogin(isLogin:Boolean,token:String?){
        val preferences = sharedPreferences.edit()
        preferences.putBoolean(LOGIN_PREF,isLogin)
        preferences.putString(USER_TOKEN,token)
        preferences.apply()
    }

    fun setUserLoginRole(role:String?){
        val preferences = sharedPreferences.edit()
        preferences.putString(SELECTED_ROLE,role)
        preferences.apply()
    }

    fun getUserLoginStatus():Boolean{
        return sharedPreferences.getBoolean(LOGIN_PREF,false)
    }

    fun getUserToken():String?{
        return sharedPreferences.getString(USER_TOKEN,null)
    }

    fun getUserLoginRole():String?{
        return sharedPreferences.getString(SELECTED_ROLE,null)
    }


}