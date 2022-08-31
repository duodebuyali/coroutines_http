package com.duode.jetpacklib.utils

import com.duode.jetpacklib.listener.NetExceptionListener

/**
 * @author hekang
 * @des 默认的服务器返回请求错误的处理
 * @date 2020/12/31 10:59
 */
internal class DefaultExceptionHandler : NetExceptionListener {
    override fun handlerException(e: Throwable) {
        println("服务器返回请求错误")
    }
}