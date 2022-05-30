package com.example.volgaitushakov.domain.module

import com.example.volgaitushakov.domain.api.FinnhubApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class GetStocksModule {

    fun getStocksApi(): Module = module{
        single<FinnhubApi>{
            createRetrofitClient("https://finnhub.io/api/v1/").create(FinnhubApi::class.java)
        }
    }

    private fun createRetrofitClient(baseUrl: String): Retrofit {
        val gson: Gson = GsonBuilder().setLenient().serializeNulls().create()
        val okHttpBuilder = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        return Retrofit.Builder()
            .client(okHttpBuilder.build())
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}