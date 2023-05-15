package com.kotlinstudy.consumer_kotlin_app.MainImageView

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.kotlinstudy.consumer_kotlin_app.R
import com.kotlinstudy.consumer_kotlin_app.TimeDiffModel.TimeDiffListData
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
import kotlin.random.Random

class MainFragment : Fragment() {
    var handler: Handler = Handler()
    var runnable: Runnable = Runnable {}

    val api by lazy { MyApi.create() }

    //Coroutine 사용
    private val scope = CoroutineScope(Dispatchers.Default)

    //timer 설정
    private var timer: Job? = null

    // 서버에서 가져온 데이터를 저장할 리스트
    private var dbDataAll: ArrayList<TimeDiffListData> = arrayListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //레이아웃 크기 구하기 위한 테스트
        val height = resources.displayMetrics.heightPixels
        val width = resources.displayMetrics.widthPixels

        val guitarview = view.findViewById<ImageView>(R.id.guitarview)
        val guitarviewScreen = view.findViewById<GridLayout>(R.id.guitarviewScreen)

        //Log.e("getRealRootViewHeight", "${getRealRootViewHeight()}") //1392
        //Log.e("getRealRootViewWidth", "${getRealRootViewWidth()}") //580
        //Log.e("height", "${height}") //1506
        //Log.e("width", "$width") //720
        //Log.e("guitarviewScreen.height", "${guitarviewScreen.height}") //1205
        //Log.e("guitarviewScreen.width", "${guitarviewScreen.width}") //720


        timer = scope.launch {
            while(true) {

                api.getData().enqueue(object : Callback<List<Datamodel>>
                {
                    //서버 요청 성공
                    override fun onResponse(call: Call<List<Datamodel>>, response: Response<List<Datamodel>>) {

                        if (response.isSuccessful) {

                            try {
                                val responseList = response.body()
                                //val resultStr = StringBuilder()

                                // List에서 하나씩 꺼내서 처리
                                responseList?.forEach { data ->

                                    // 새로운 값을 리스트에 추가
                                    //dbDataAll.add(TimeDiffListData("${data.logId}", formattedDateTime.toString()))

                                    val sensor_X_speed = data.sensorX!!.toFloat() * Random.nextInt(1, 2).toFloat()
                                    val sensor_Y_speed = data.sensorY!!.toFloat() * Random.nextInt(1, 2).toFloat()

                                    //Log.e("sensor_X_speed", "sensor_X_speed: $sensor_X_speed")
                                    //Log.e("sensor_Y_speed", "sensor_Y_speed: $sensor_Y_speed")


                                    guitarview.translationX -= sensor_X_speed
                                    guitarview.translationY += sensor_Y_speed

                                    //screen 밖으로 guitarview가 벗어나지 않게 설정. 확인 후 수정할 가능성 있음.

                                    //if (guitarview.translationY > getRealRootViewHeight()) guitarview.translationY = getRealRootViewHeight().toFloat()
                                    //if (guitarview.translationY < 0) guitarview.translationY = 0f

                                    //if (guitarview.translationX > getRealRootViewWidth()) guitarview.translationX = getRealRootViewWidth().toFloat()
                                    //if (guitarview.translationX < 0) guitarview.translationX = 0f

                                    if (guitarview.translationY > guitarviewScreen.height - 140) guitarview.translationY = guitarviewScreen.height.toFloat() - 140
                                    if (guitarview.translationY < 0) guitarview.translationY = 0f

                                    if (guitarview.translationX > guitarviewScreen.width - 140) guitarview.translationX = guitarviewScreen.width.toFloat() - 140
                                    if (guitarview.translationX < 0) guitarview.translationX = 0f

                                    Log.e("guitarview X위치", "guitarview X위치: ${guitarview.translationX}")
                                    Log.e("guitarview Y위치", "guitarview Y위치: ${guitarview.translationY}")

                                }

                            }

                            catch (e: JSONException) {
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

                delay(100 /*10초: 10000*/)
            }
        }

    }

    private fun getRealRootViewWidth(): Int {
        return requireActivity().window.decorView.width - 140
    }

    private fun getRealRootViewHeight(): Int {
        return if (Build.VERSION.SDK_INT < 30) {
            requireActivity().window.decorView.height - 140 - requireActivity().window.decorView.rootWindowInsets.run {
                systemWindowInsetTop + systemWindowInsetBottom
            }
        } else {
            val insets = requireActivity().window.decorView.rootWindowInsets.displayCutout?.run {
                safeInsetBottom + safeInsetTop
            } ?: 0
            requireActivity().window.decorView.height - 140 - insets
        }
    }
}