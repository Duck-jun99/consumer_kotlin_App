package com.kotlinstudy.consumer_kotlin_app

import android.animation.ObjectAnimator
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
import androidx.navigation.fragment.findNavController
import com.kotlinstudy.consumer_kotlin_app.RvModel.dbListData
import java.lang.Float.max
import java.lang.Float.min
import kotlin.random.Random

class MainFragment : Fragment() {
    var handler: Handler = Handler()
    var runnable: Runnable = Runnable {}

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val guitarview = view.findViewById<ImageView>(R.id.guitarview)
        val guitarviewScreen = view.findViewById<GridLayout>(R.id.guitarviewScreen)

        val bundle = requireActivity().intent?.extras
        Log.e("bundle", bundle.toString())

        if (bundle != null && bundle.containsKey("DB_Data_all")) {
            val dbDataAll = bundle.getSerializable("DB_Data_all") as ArrayList<dbListData>
            Log.e("dbDataAll", "DB_Data_List: $dbDataAll")

            var currentIndex = 0

            runnable = object : Runnable{
                override fun run() {
                    Log.e("dbDataAll Size", "dbDataAll size: ${dbDataAll.size}")

                    if (currentIndex < dbDataAll.size) {
                        val sensor_X_speed = dbDataAll.get(currentIndex).Sensor_x.toFloat() * Random.nextInt(2, 10).toFloat()
                        val sensor_Y_speed = dbDataAll.get(currentIndex).Sensor_y.toFloat() * Random.nextInt(2, 10).toFloat()

                        Log.e("sensor_X_speed", "sensor_X_speed: $sensor_X_speed")
                        Log.e("sensor_Y_speed", "sensor_Y_speed: $sensor_Y_speed")


                        guitarview.translationX -= sensor_X_speed
                        guitarview.translationY += sensor_Y_speed

                        //screen 밖으로 guitarview가 벗어나지 않게 설정. 확인 후 수정할 가능성 있음.
                        if (guitarview.translationY > getRealRootViewHeight()) guitarview.translationY = getRealRootViewHeight().toFloat()
                        if (guitarview.translationY < 0) guitarview.translationY = 0f

                        if (guitarview.translationX > getRealRootViewWidth()) guitarview.translationX = getRealRootViewWidth().toFloat()
                        if (guitarview.translationX < 0) guitarview.translationX = 0f

                        Log.e("guitarview X위치", "guitarview X위치: ${guitarview.translationX.toString()}")
                        Log.e("guitarview Y위치", "guitarview Y위치: ${guitarview.translationY.toString()}")

                        currentIndex++
                    }

                    if (currentIndex < dbDataAll.size) {
                        handler.postDelayed(this, 100L)
                    }
                }
            }
            handler.post(runnable)
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