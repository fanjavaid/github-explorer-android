package com.fanjavaid.github_explorer_android.data.network.config

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Fandi Akhmad (fanjavaid) on 23/08/20.
 */
object RetrofitApi {
    private fun getApi() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> service(serviceClass: Class<T>): T {
        return getApi().create(serviceClass)
    }
}
