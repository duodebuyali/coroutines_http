package com.codemao.coroutinenet.test.net

import androidx.lifecycle.LiveData
import com.codemao.coroutinenet.test.bean.Weather
import com.codemao.coroutinenet.test.const.ApiConst
import com.duode.dlog.test.net.TestApi
import com.duode.jetpacklib.bean.BaseResponse
import com.duode.netlibrary.ApiManager
import com.duode.netlibrary.bean.ApiResponse
import kotlinx.coroutines.flow.Flow

/**
 * @author hekang
 * @des
 * @date 2020/12/8 15:47
 */
class TestApiStore : ApiManager<TestApi>() {

    override val createService: () -> TestApi = {
        mRetrofit.create(TestApi::class.java)
    }

    override val baseUrl: String = ApiConst.BASE_URL

    fun getWeatherLiveData(): LiveData<ApiResponse<BaseResponse<Weather>>> {
        return mService.fetchWeather()
    }

    fun getWeatherFlow(): Flow<ApiResponse<BaseResponse<Weather>>> {
        return mService.pullWeather()
    }

    suspend fun getWeather(): BaseResponse<Weather> {
        return mService.forceWeather()
    }
    suspend fun fetchWeather(): ApiResponse<BaseResponse<Weather>> {
        return mService.getWeather()
    }
}