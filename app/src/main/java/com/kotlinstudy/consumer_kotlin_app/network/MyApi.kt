package com.kotlinstudy.consumer_kotlin_app.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kotlinstudy.consumer_kotlin_app.Secret
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
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

            // Moshi 사용 안함.
            //val moshi: Moshi = Moshi.Builder().build()
            //val converterFactory: MoshiConverterFactory = MoshiConverterFactory.create(moshi)

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