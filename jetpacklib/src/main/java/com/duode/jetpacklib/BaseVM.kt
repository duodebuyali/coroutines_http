package com.duode.jetpacklib

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.duode.jetpacklib.utils.CommonObserverManager
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * @author hekang
 * @des ViewModel的基类
 * @date 2020/12/7 17:51
 */
open class BaseVM(private val mDispatcher: CoroutineDispatcher = Dispatchers.Main) : ViewModel(),
    LifecycleObserver, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = mDispatcher


    /**
     * 用来管理job
     * */
    private val mLaunchManager: MutableList<Job> = mutableListOf()

    /**
     * 统一进行异常处理
     * @param tryBlock 耗时任务,默认在子线程中进行的协程方法
     * */
    protected fun CoroutineScope.launchOnUITryCatch(
        isChangeContext: Boolean = true,
        tryBlock: suspend CoroutineScope.() -> Unit
    ) {
        launchOnUI {
            doWithTryCatch({ CommonObserverManager.onStart?.invoke() },
                tryBlock,
                { CommonObserverManager.onCompleted?.invoke() },
                {
                    handleException(it)
                }, isChangeContext)
        }
    }

    /**
     * 统一进行异常处理
     * @param startBlock 默认在主线程中进行的协程方法
     * @param tryBlock 默认在子线程中进行的协程方法
     * @param finallyBlock 任务完成或异常之后，都会进行的方法
     * */
    protected fun CoroutineScope.launchOnUITryCatch(
        startBlock: suspend CoroutineScope.() -> Unit,
        tryBlock: suspend CoroutineScope.() -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        isChangeContext: Boolean = true
    ) {
        launchOnUI {
            doWithTryCatch(startBlock, tryBlock, finallyBlock, {
                handleException(it)
            }, isChangeContext)
        }
    }

    /**
     * 需要自己进行异常处理
     * @param startBlock 默认在主线程中进行的协程方法
     * @param tryBlock 默认在子线程中进行的协程方法
     * @param finallyBlock 任务完成或异常之后，都会进行的方法
     * @param catchBlock 单独的异常处理方法
     * @param isChangeContext 是否使用withContext切换CoroutineContext
     * */
    protected fun CoroutineScope.launchOnUITryCatch(
        startBlock: suspend CoroutineScope.() -> Unit,
        tryBlock: suspend CoroutineScope.() -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        isChangeContext: Boolean = true
    ) {
        launchOnUI {
            doWithTryCatch(startBlock, tryBlock, finallyBlock, catchBlock, isChangeContext)
        }
    }

    /**
     * 记录job
     */
    private fun CoroutineScope.launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        val job = launch { block() }
        mLaunchManager.add(job)
        job.invokeOnCompletion {
            mLaunchManager.remove(job)
        }

    }

    private suspend fun CoroutineScope.doWithTryCatch(
        startBlock: suspend CoroutineScope.() -> Unit,
        tryBlock: suspend CoroutineScope.() -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        isChangeContext: Boolean = true
    ) {
        try {
            startBlock()
            // 对这里的执行任务进行细分，如果需要在后台执行的任务，单独再创建一个CoroutineScope
            if (isChangeContext) {
                withContext(Dispatchers.IO) {
                    tryBlock()
                }
            } else {
                tryBlock()
            }

        } catch (e: Throwable) {
            if (e is CancellationException) {
                //任务被取消
            } else {
                //统一处理错误
                catchBlock(e)
            }
        } finally {
            finallyBlock()
        }
    }

    /**
     * 移除job
     * @see LifecycleObserver
     * */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        clearLaunchTask()
    }

    private fun clearLaunchTask() {
        mLaunchManager.forEach {
            it.cancel()
        }
        mLaunchManager.clear()
    }

    /**
     * 默认的统一处理异常的情况
     * */
    protected fun handleException(e: Throwable) {
        //一般就是记录错误信息，并提示用户即可
        e.printStackTrace()
    }

}