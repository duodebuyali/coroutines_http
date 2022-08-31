package com.duode.jetpacklib.utils

import com.duode.jetpacklib.listener.NetExceptionListener

/**
 * @author hekang
 * @des 网络请求服务器返回的逻辑错误管理
 * @date 2020/12/31 09:28
 */
object NetExceptionManager {
    var mNetExceptionListener: NetExceptionListener? = null
}