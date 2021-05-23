package com.example.android.politicalpreparedness.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class CivicsHttpClient: OkHttpClient() {

    companion object {

        const val API_KEY = "AIzaSyCRi_YOeKRC-ud2MVceWm3K5T32eBUg95s" //TODO: Place your API Key Here

        fun getClient(): OkHttpClient {
            return Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                })
                .addInterceptor { chain ->
                    val original = chain.request()
                    val url = original
                            .url
                            .newBuilder()
                            .addQueryParameter("key", API_KEY)
                            .build()
                    val request = original
                            .newBuilder()
                            .url(url)
                            .build()
                    chain.proceed(request)
                }
                .build()
        }
    }
}
