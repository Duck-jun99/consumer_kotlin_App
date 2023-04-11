package com.kotlinstudy.consumer_kotlin_app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.kotlinstudy.consumer_kotlin_app.network.Datamodel
import com.kotlinstudy.consumer_kotlin_app.network.MyApi
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    val api by lazy { MyApi.create() }

    private lateinit var tv_logId : TextView
    private lateinit var tv_sensorX : TextView
    private lateinit var tv_sensorY : TextView
    private lateinit var tv_sensorZ : TextView
    private lateinit var et_post_value : EditText

    private lateinit var btn_dbdatalist : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //tv_logId = findViewById(R.id.tv_logId)
        //tv_sensorX = findViewById(R.id.tv_sensorX)
        //tv_sensorY = findViewById(R.id.tv_sensorY)
        //tv_sensorZ = findViewById(R.id.tv_sensorZ)

        et_post_value = findViewById<EditText>(R.id.et_post_value)
        //btn_post_tv = findViewById<Button>(R.id.btn_post)
        btn_dbdatalist = findViewById<Button>(R.id.btn_dbdatalist)



        btn_dbdatalist.setOnClickListener {

            if (et_post_value.text.toString() == "7"){

                api.getData().enqueue(object : Callback<List<Datamodel>>
                {
                    //서버 요청 성공
                    override fun onResponse(call: Call<List<Datamodel>>, response: Response<List<Datamodel>>) {

                        if (response.isSuccessful) {

                            //서버와 통신이 성공적으로 이뤄졌을 떄
                            // 여기서 주요 기능을 모두 수행
                            try {
                                val responseList = response.body()
                                val resultStr = StringBuilder()

                                var dbDataAll: ArrayList<dbListData> = arrayListOf()

                                // List에서 하나씩 꺼내서 처리
                                responseList?.forEach { data ->

                                    resultStr.append("logId: ${data.logId},\n, Sensor_x: ${data.sensorX}, \n, Sensor_y: ${data.sensorY}, \n, Sensor_z: ${data.sensorZ}\n")
                                    Log.d("logId", "${data.logId}")
                                    Log.d("sensorX", "${data.sensorX}")
                                    Log.d("sensorY", "${data.sensorY}")
                                    Log.d("sensorZ", "${data.sensorZ}")

                                    //dbDataFragment로 intent해서 보내기 위한 dbDataAll 생성 과정
                                    dbDataAll.add(dbListData("${data.logId}", "${data.sensorX}","${data.sensorY}","${data.sensorZ}"))
                                }

                                // 결과 문자열을 텍스트 뷰에 출력
                                //tv_logId.text = resultStr
                                Toast.makeText(this@MainActivity, "서버 통신 완료", Toast.LENGTH_SHORT).show()

                                //MainActvity의 Data를 dbDataFragment로 intent
                                intent.putExtra("DB_Data_all", dbDataAll)

                                //dbDataFragment 생성 코드 시작
                                val fragmet_dbDataList = supportFragmentManager.beginTransaction()
                                fragmet_dbDataList.replace(
                                    R.id.framelayout1,
                                    dbDataFragment()
                                )
                                fragmet_dbDataList.commit()
                                //dbDataFragment 생성 코드 종료
                            }

                            catch (e: JSONException) {
                                tv_logId.text = e.message
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
            else{ Toast.makeText(this, "Password값 틀림", Toast.LENGTH_SHORT).show() }
        }



    }

}
