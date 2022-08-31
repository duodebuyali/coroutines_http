package com.duode.netlibrary.listener

import com.duode.netlibrary.bean.ApiException

/**
 * @author hekang
 * @des 监听网络请求错误的情况
 * @date 2020/12/31 10:46
 */
interface NetErrorListener {
    fun handleException(e: Throwable): ApiException
}