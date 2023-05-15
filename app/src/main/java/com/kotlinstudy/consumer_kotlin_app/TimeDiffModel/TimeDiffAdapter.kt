package com.kotlinstudy.consumer_kotlin_app.TimeDiffModel

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.kotlinstudy.consumer_kotlin_app.R
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimeDiffAdapter(public var datas: ArrayList<TimeDiffListData>) : RecyclerView.Adapter<TimeDiffAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.time_diff_recyclerview,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val TvServerGetTime: TextView = itemView.findViewById(R.id.tv_server_get_time)
        private val TvRealGetTime: TextView = itemView.findViewById(R.id.tv_real_get_time)
        private val DiffTime: TextView = itemView.findViewById(R.id.diff_time)

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(item: TimeDiffListData) {

            TvServerGetTime.text = "서버 저장 시간: " + item.Server_Get_time
            TvRealGetTime.text = "실제 받아온 시간: " + item.Real_Get_time

            val formatter = DateTimeFormatter.ofPattern("yyyy/M/d/H/m/s/SSS")
            val timeA = LocalDateTime.parse(item.Server_Get_time, formatter)
            val timeB = LocalDateTime.parse(item.Real_Get_time, formatter)

            val duration = Duration.between(timeA, timeB)
            val diffInMillis = duration.toMillis()

            val minutes = duration.toMinutes()
            val seconds = duration.seconds % 60
            val millis = diffInMillis % 1000

            DiffTime.text = "시간 차이:\n ${minutes}분 ${seconds}초 ${millis}밀리초"

        }
    }


}