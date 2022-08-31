package com.duode.jetpacklib.listener

/**
 * @author hekang
 * @des 网络请求逻辑错误监听
 * @date 2020/12/31 09:27
 */
interface NetExceptionListener {

    fun handlerException(e: Throwable)
}