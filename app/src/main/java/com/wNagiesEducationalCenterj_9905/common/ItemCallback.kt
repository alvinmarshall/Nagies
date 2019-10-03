package com.wNagiesEducationalCenterj_9905.common

interface ItemCallback<T> {
    fun onClick(data: T?)
    fun onHold(data: T?)
}