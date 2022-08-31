package com.codemao.coroutinenet

import android.app.Application
import com.duode.jetpacklib.utils.CommonObserverManager

/**
 * @author hekang
 * @des
 * @date 2020/12/24 09:24
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //配置默认统一的调用方法
        CommonObserverManager.onStart = {
            println("CommonObserverManager--onStart")
        }
    }
}