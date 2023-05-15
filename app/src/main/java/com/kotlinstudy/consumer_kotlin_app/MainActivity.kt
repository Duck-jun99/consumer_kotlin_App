package com.kotlinstudy.consumer_kotlin_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.kotlinstudy.consumer_kotlin_app.DBData.dbDataFragment
import com.kotlinstudy.consumer_kotlin_app.GuitarModel.GuitarModel
import com.kotlinstudy.consumer_kotlin_app.DBData.dbListData
import com.kotlinstudy.consumer_kotlin_app.MainImageView.MainFragment
import com.kotlinstudy.consumer_kotlin_app.TimeDiffModel.TimeDiffFragment
import com.kotlinstudy.consumer_kotlin_app.databinding.ActivityMainBinding
import com.kotlinstudy.consumer_kotlin_app.Network.Datamodel
import com.kotlinstudy.consumer_kotlin_app.Network.MyApi
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val api by lazy { MyApi.create() }

    private lateinit var tv_logId : TextView
    private lateinit var tv_sensorX : TextView
    private lateinit var tv_sensorY : TextView
    private lateinit var tv_sensorZ : TextView

    private lateinit var btn_dbdatalist_open : Button
    private lateinit var btn_main_screen : Button
    private lateinit var btn_debug : Button

    private var timer: Timer? = null

    private lateinit var guitarView : ImageView

    private val fallingModels: MutableList<GuitarModel> = mutableListOf()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //btn_post_tv = findViewById<Button>(R.id.btn_post)
        btn_dbdatalist_open = findViewById<Button>(R.id.btn_dbdatalist_open)
        btn_main_screen = findViewById<Button>(R.id.btn_main_screen)
        btn_debug = findViewById<Button>(R.id.btn_debug)


        btn_dbdatalist_open.setOnClickListener {

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
                                //Log.d("logId", "${data.logId}")
                                //Log.d("sensorX", "${data.sensorX}")
                                //Log.d("sensorY", "${data.sensorY}")
                                //Log.d("sensorZ", "${data.sensorZ}")

                                //dbDataFragment로 intent해서 보내기 위한 dbDataAll 생성 과정
                                dbDataAll.add(dbListData("${data.logId}", "${data.sensorX}","${data.sensorY}","${data.sensorZ}"))
                            }

                            // 결과 문자열을 텍스트 뷰에 출력
                            //tv_logId.text = resultStr
                            Toast.makeText(this@MainActivity, "서버 통신 완료", Toast.LENGTH_SHORT).show()

                            //MainActvity의 Data를 dbDataFragment로 intent
                            intent.putExtra("DB_Data_all", dbDataAll)

                            //dbDataFragment 생성 코드 시작
                            val fragment_dbDataList = supportFragmentManager.beginTransaction()
                            fragment_dbDataList.replace(
                                R.id.framelayout1,
                                dbDataFragment()
                            )
                            fragment_dbDataList.commit()
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

        btn_main_screen.setOnClickListener {

            val fragment = supportFragmentManager.findFragmentById(R.id.framelayout1)
            if (fragment !is MainFragment) {
                //dbDataFragment 삭제
                //supportFragmentManager.beginTransaction().remove(fragment).commit()

                //MainFragment 생성
                val fragment_main = supportFragmentManager.beginTransaction()
                fragment_main.replace(
                    R.id.framelayout1,
                    MainFragment()
                )
                fragment_main.commit()
                //MainFragment 생성 코드 종료
            }
            
            else { Toast.makeText(this, "GET 요청 버튼을 눌러주세요", Toast.LENGTH_SHORT).show() }
        }

        btn_debug.setOnClickListener {

            val fragment = supportFragmentManager.findFragmentById(R.id.framelayout1)
            if (fragment !is TimeDiffFragment) {

                //TimeDiffFragment 생성
                val fragment_main = supportFragmentManager.beginTransaction()
                fragment_main.replace(
                    R.id.framelayout1,
                    TimeDiffFragment()
                )
                fragment_main.commit()
                //TimeDiffFragment 생성 코드 종료
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
    }

}
