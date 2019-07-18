package com.wNagiesEducationalCenterj_9905.common

fun showAnyView(view: Any?, message:String?,listener:Any?,visible:Boolean = false, Func:(Any?, String?,Any?,Boolean) -> Unit){
    Func(view,message,listener,visible)
}