package com.kotlinstudy.consumer_kotlin_app

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.kotlinstudy.consumer_kotlin_app.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.reflect.typeOf

class MainActivity : AppCompatActivity() {

    val api by lazy { MyApi.create() }

    private lateinit var tv : TextView
    private lateinit var et_post_value : EditText
    private lateinit var btn_post_tv : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.tv)
        et_post_value = findViewById<EditText>(R.id.et_post_value)
        btn_post_tv = findViewById<Button>(R.id.btn_post)



        btn_post_tv.setOnClickListener {
            if (et_post_value.text.toString() == "7"){
                api.getData().enqueue(object : Callback<List<Datamodel>>
                {
                    //서버 요청 성공
                    override fun onResponse(call: Call<List<Datamodel>>, response: Response<List<Datamodel>>) {
                        //Log.e("Successful Message: ", "데이터 성공적으로 수신")
                        //Log.e("Result: ", response.body().toString())
                        //tv.text = response.body().toString()

                        if (response.isSuccessful) {

                            try {
                                val responseList = response.body()
                                val resultStr = StringBuilder()

                                // List에서 하나씩 꺼내서 처리
                                responseList?.forEach { data ->
                                    resultStr.append("logId: ${data.logId}, Sensor_x: ${data.sensorX}\n")
                                }

                                // 결과 문자열을 텍스트 뷰에 출력
                                tv.text = resultStr
                            }
                            catch (e: JSONException) {
                                tv.text = e.message
                                Log.e("JSONException", e.message.toString())
                            }

                        }

                    }
                    //서버 요청 실패
                    override fun onFailure(call: Call<List<Datamodel>>, t: Throwable)
                    {
                        Log.e("Error Message : ",  t.message.toString())
                    }
                })

                runOnUiThread {
                    Toast.makeText(this@MainActivity, "POST", Toast.LENGTH_SHORT).show()
                }
            }
            else{ Toast.makeText(this, "Post값 틀림", Toast.LENGTH_SHORT).show() }
        }

    }

}
