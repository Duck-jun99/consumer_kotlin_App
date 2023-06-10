package com.kotlinstudy.consumer_kotlin_app.MainImageView

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
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
import org.w3c.dom.Text
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

    private var Score: Int? = 0


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

    @SuppressLint("CutPasteId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //레이아웃 크기 구하기 위한 테스트
        val height = resources.displayMetrics.heightPixels
        val width = resources.displayMetrics.widthPixels

        val guitarview = view.findViewById<ImageView>(R.id.guitarview)
        val guitarviewScreen = view.findViewById<GridLayout>(R.id.guitarviewScreen)

        val tv_score = view.findViewById<TextView>(R.id.score)
        val tv_end = view.findViewById<TextView>(R.id.tv_end)
        val btn_start = view.findViewById<Button>(R.id.start_get)
        val btn_stop = view.findViewById<Button>(R.id.stop_get)
        val btn_back = view.findViewById<Button>(R.id.btn_back)

        val imageView = ImageView(requireContext())
        imageView.setImageResource(R.drawable.human_80)

        val layoutParams = GridLayout.LayoutParams()
        layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT
        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT
        layoutParams.setGravity(Gravity.CENTER)

        val randomX = (Math.random() * (guitarviewScreen.width - imageView.width)).toInt()
        layoutParams.leftMargin = randomX

        val randomY = (Math.random() * (guitarviewScreen.height - imageView.height)).toInt()
        layoutParams.topMargin = randomY

        guitarviewScreen.addView(imageView, layoutParams)

        btn_start.setOnClickListener {

            timer = scope.launch {
                while(Score!! < 3) {

                    api.getData().enqueue(object : Callback<List<Datamodel>>
                    {
                        //서버 요청 성공
                        override fun onResponse(call: Call<List<Datamodel>>, response: Response<List<Datamodel>>) {

                            if (response.isSuccessful) {

                                try {
                                    var responseList = response.body()

                                    if (responseList != null) {
                                        responseList = listOf(responseList.last())
                                    }

                                    // List에서 하나씩 꺼내서 처리
                                    responseList?.forEach { data ->

                                        val sensor_X_speed = data.sensorX!!.toFloat() * Random.nextInt(1, 2).toFloat()
                                        val sensor_Y_speed = data.sensorY!!.toFloat() * Random.nextInt(1, 2).toFloat()


                                        guitarview.translationX -= sensor_X_speed
                                        guitarview.translationY += sensor_Y_speed

                                        //screen 밖으로 guitarview가 벗어나지 않게 설정. 확인 후 수정할 가능성 있음.

                                        if (guitarview.translationY > guitarviewScreen.height - 40) guitarview.translationY = guitarviewScreen.height.toFloat() - 140
                                        if (guitarview.translationY < 0) guitarview.translationY = 0f

                                        if (guitarview.translationX > guitarviewScreen.width - 40) guitarview.translationX = guitarviewScreen.width.toFloat() - 140
                                        if (guitarview.translationX < 0) guitarview.translationX = 0f


                                        // 이미지뷰의 좌표
                                        val imageViewX = imageView.x + imageView.translationX
                                        val imageViewY = imageView.y + imageView.translationY

                                        // guitarview와 사람 이미지가 맞닿았을 때 score+1, 이미지 변경
                                        if (imageViewX >= guitarview.x && imageViewX <= guitarview.x + guitarview.width &&
                                            imageViewY >= guitarview.y && imageViewY <= guitarview.y + guitarview.height) {
                                            Score = Score?.plus(1)


                                            if (Score == 0) {
                                                imageView.setImageResource(R.drawable.human_80)
                                            }
                                            else if (Score == 3) {
                                                imageView.setImageResource(R.drawable.student_80)
                                                tv_end.visibility = View.VISIBLE

                                            }

                                            tv_score.text = "상태: 졸업"


                                        }

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

        btn_stop.setOnClickListener {
            timer?.cancel()
            timer = null
        }


        btn_back.setOnClickListener {
            val fragment = requireFragmentManager().findFragmentById(R.id.framelayout1) as? MainFragment
            fragment?.let {
                requireFragmentManager().beginTransaction().remove(it).commit()
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
    }

    override fun onStop(){
        super.onStop()
        timer?.cancel()
        timer = null
    }
}