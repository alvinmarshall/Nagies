package com.wNagiesEducationalCenterj_9905.common.delegate

import kotlinx.coroutines.*

fun<T> lazyDeferred(block:suspend CoroutineScope.() -> T):Lazy<Deferred<T>>{
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {block.invoke(this)  }
    }
}