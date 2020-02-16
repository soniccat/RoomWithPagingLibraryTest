package com.sample.roomandpaginlibrary.net

import okhttp3.*
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

object LingvoLive {
    fun createLingvoLiveClient(): Retrofit {
        val client = OkHttpClient.Builder().addInterceptor(object : Interceptor {
            var token =
                ""

            override fun intercept(chain: Interceptor.Chain): Response {
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                return chain.proceed(newRequest)
            }
        }).build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://developers.lingvolive.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createLingvoLiveService(): LingvoLiveService {
        return createLingvoLiveClient().create(LingvoLiveService::class.java)
    }
}