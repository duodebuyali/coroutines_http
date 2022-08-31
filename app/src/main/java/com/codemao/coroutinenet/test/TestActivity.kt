package com.codemao.coroutinenet.test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.codemao.coroutinenet.R
import com.codemao.coroutinenet.databinding.ActivityTestBinding
import com.duode.jetpacklib.BaseVMActivity
import kotlinx.android.synthetic.main.activity_test.*

/**
 * @author hekang
 * @des
 * @date 2020/12/8 15:33
 */
class TestActivity : BaseVMActivity<TestVM>() {

    override val providerVMClass: Class<TestVM> = TestVM::class.java

    private val mDB by binding<ActivityTestBinding>(R.layout.activity_test)

    companion object {
        fun getCallIntent(ctx: Context): Intent {
            return Intent(ctx, TestActivity::class.java)
        }

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, TestActivity::class.java)
                .apply {

                }
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDB.lifecycleOwner = this
        mDB.vm = mVM

        btn_suspend.setOnClickListener {
            mVM.getWeather({ println("btn_suspend--onStart")}, { })
        }

    }


}