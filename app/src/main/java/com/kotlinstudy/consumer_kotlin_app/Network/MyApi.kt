package com.kotlinstudy.consumer_kotlin_app.Network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kotlinstudy.consumer_kotlin_app.Secret
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

interface MyApi {
    @GET(Secret.PHP_FILE)
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun getData(): Call<List<Datamodel>>

    companion object {
        fun create(): MyApi {
            val gson: Gson = GsonBuilder().setLenient().create()

            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            return Retrofit.Builder()
                .baseUrl(Secret.MY_IP_ADDRESS)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
              //  .addConverterFactory(converterFactory)
                .build()
                .create(MyApi::class.java)
        }
    }
}