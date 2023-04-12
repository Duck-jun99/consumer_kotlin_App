package com.kotlinstudy.consumer_kotlin_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.kotlinstudy.consumer_kotlin_app.GuitarModel.GuitarModel
import com.kotlinstudy.consumer_kotlin_app.RvModel.DBdataAdapter
import com.kotlinstudy.consumer_kotlin_app.RvModel.dbListData
import com.kotlinstudy.consumer_kotlin_app.databinding.ActivityMainBinding
import com.kotlinstudy.consumer_kotlin_app.network.Datamodel
import com.kotlinstudy.consumer_kotlin_app.network.MyApi
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val api by lazy { MyApi.create() }

    private lateinit var tv_logId : TextView
    private lateinit var tv_sensorX : TextView
    private lateinit var tv_sensorY : TextView
    private lateinit var tv_sensorZ : TextView
    private lateinit var et_post_value : EditText

    private lateinit var btn_dbdatalist_open : Button
    private lateinit var btn_main_screen : Button

    private var timer: Timer? = null

    private lateinit var guitarView : ImageView

    private val fallingModels: MutableList<GuitarModel> = mutableListOf()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        et_post_value = findViewById<EditText>(R.id.et_post_value)
        //btn_post_tv = findViewById<Button>(R.id.btn_post)
        btn_dbdatalist_open = findViewById<Button>(R.id.btn_dbdatalist_open)
        btn_main_screen = findViewById<Button>(R.id.btn_main_screen)



        btn_dbdatalist_open.setOnClickListener {

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
            else{ Toast.makeText(this, "Password값 틀림", Toast.LENGTH_SHORT).show() }
        }

        btn_main_screen.setOnClickListener {


            val fragment = supportFragmentManager.findFragmentById(R.id.framelayout1)
            if (fragment is dbDataFragment) {
                //dbDataFragment 삭제
                supportFragmentManager.beginTransaction().remove(fragment).commit()

                //MainFragment 생성
                val fragment_main = supportFragmentManager.beginTransaction()
                fragment_main.replace(
                    R.id.framelayout1,
                    MainFragment()
                )
                fragment_main.commit()
                //MainFragment 생성 코드 종료
            }
            else if(fragment is MainFragment)
            {
                Toast.makeText(this, "현재 메인페이지입니다.", Toast.LENGTH_SHORT).show()
            }
            else { Toast.makeText(this, "먼저 데이터 리스트 열기 버튼을 눌러주세요", Toast.LENGTH_SHORT).show() }


            /*
            // dbDataList fragment가 열려 있다면 닫기
            val fragment = supportFragmentManager.findFragmentById(R.id.framelayout1)
            if (fragment is dbDataFragment) {
                supportFragmentManager.beginTransaction().remove(fragment).commit()

            }
            else{ Toast.makeText(this, "현재 메인페이지입니다.", Toast.LENGTH_SHORT).show() }

             */

            /*
            val dbDataAll = intent.getSerializableExtra("DB_Data_all") as ArrayList<dbListData>?

            // DB 데이터가 전달된 경우
            if (dbDataAll != null) {


                //GuitarView.setBackgroundResource(R.drawable.guitar)
                val guitar = ImageView(this).apply {
                    setBackgroundResource(R.drawable.guitar)
                    layoutParams = LinearLayout.LayoutParams(GUITAR_SIZE, GUITAR_SIZE)}

                for (i in 0 until dbDataAll.size) {
                    //Log.e("dbDataAll[${i}].Lod_ID",dbDataAll[i].Lod_ID)
                    //initTouchListener() -> 필요 없으면 지울 것

                    val sensor_x = dbDataAll[i].Sensor_x.toFloat() * Random.nextInt(2, 10).toFloat()
                    val sensor_y = dbDataAll[i].Sensor_y.toFloat() * Random.nextInt(2, 10).toFloat()


                    Log.e("sensor_x: ", sensor_x.toString())
                    Log.e("sensor_y: ", sensor_y.toString())
                    Log.e("guitar.x: ", guitar.x.toString())
                    Log.e("guitar.y: ", guitar.y.toString())

                }

                val adapter = DBdataAdapter(dbDataAll)
                //데이터 지우는 코드
                if (adapter.itemCount > 0) {
                    adapter.datas.removeAt(0)
                    adapter.notifyItemRemoved(0)
                }
            }
            // DB 데이터가 전달되지 않은 경우
            else {


            }
            */

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
    }

}
