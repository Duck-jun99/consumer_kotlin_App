package com.kotlinstudy.consumer_kotlin_app

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
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
    fun getData(

        //@Body data: PostModel
        //@Field를 사용하고 @FormUrlEncoded를 사용해야 올바른 Json형식으로 넘어감
    ): Call<List<Datamodel>>

    companion object {
        fun create(): MyApi {
            val gson: Gson = GsonBuilder().setLenient().create()

            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val moshi: Moshi = Moshi.Builder().build()
            val converterFactory: MoshiConverterFactory = MoshiConverterFactory.create(moshi)

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