package com.kotlinstudy.consumer_kotlin_app.TimeDiffModel

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlinstudy.consumer_kotlin_app.R
import com.kotlinstudy.consumer_kotlin_app.Network.Datamodel
import com.kotlinstudy.consumer_kotlin_app.Network.MyApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TimeDiffFragment : Fragment() {

    val api by lazy { MyApi.create() }

    // RecyclerView.adapter에 지정할 Adapter
    private lateinit var timeDiffAdapter: TimeDiffAdapter

    private lateinit var start_get: Button
    private lateinit var stop_get: Button
    private lateinit var time_diff_mean: TextView

    //Coroutine 사용
    private val scope = CoroutineScope(Dispatchers.Default)

    //timer 설정
    private var timer: Job? = null

    // 서버에서 가져온 데이터를 저장할 리스트
    private var dbDataAll: ArrayList<TimeDiffListData> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_diff, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        start_get = view.findViewById(R.id.start_get)
        stop_get = view.findViewById(R.id.stop_get)

        time_diff_mean = view.findViewById(R.id.time_diff_mean)

        timeDiffAdapter = TimeDiffAdapter(dbDataAll)
        val rv_dbdata = view.findViewById<RecyclerView>(R.id.rv_timediff)
        rv_dbdata.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rv_dbdata.adapter = timeDiffAdapter

        start_get.setOnClickListener {

            timer = scope.launch {
                while(true) {

                    val timestamp = System.currentTimeMillis()
                    val formatter = DateTimeFormatter.ofPattern("yyyy/M/d/H/m/s/SSS")
                    val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
                    val formattedDateTime = dateTime.format(formatter)
                    //val result = "$formattedDateTime${timestamp.toString().substring(10)}"

                    api.getData().enqueue(object : Callback<List<Datamodel>>
                    {
                        //서버 요청 성공
                        @SuppressLint("SetTextI18n")
                        override fun onResponse(call: Call<List<Datamodel>>, response: Response<List<Datamodel>>) {

                            if (response.isSuccessful) {

                                try {
                                    val responseList = response.body()
                                    val resultStr = StringBuilder()

                                    // List에서 하나씩 꺼내서 처리
                                    responseList?.forEach { data ->

                                        // 새로운 값을 리스트에 추가
                                        dbDataAll.add(TimeDiffListData("${data.logId}", formattedDateTime.toString()))

                                        // 어댑터에게 데이터 변경 알림
                                        timeDiffAdapter.notifyDataSetChanged()

                                        val serverTime = LocalDateTime.parse(data.logId, formatter)
                                        val realTime = LocalDateTime.parse(formattedDateTime, formatter)
                                        val duration = Duration.between(serverTime, realTime)
                                        val diffInMillis = duration.toMillis()

                                        val minutes = duration.toMinutes()
                                        val seconds = duration.seconds % 60
                                        val millis = diffInMillis % 1000

                                        time_diff_mean.text = "시간 차이 평균:\n ${minutes}분 ${seconds}초 ${millis}밀리초"
                                    }

                                }

                                catch (e: JSONException) {
                                    time_diff_mean.text = e.message
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

                    delay(500 /*10초: 10000*/)
                }
            }
        }

        stop_get.setOnClickListener {
            //타이머 종료
            timer?.cancel()
            timer = null
        }
    }
}