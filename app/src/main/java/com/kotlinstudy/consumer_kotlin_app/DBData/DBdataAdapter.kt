package com.kotlinstudy.consumer_kotlin_app.DBData

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlinstudy.consumer_kotlin_app.R

class DBdataAdapter(public var datas: MutableList<dbListData>) : RecyclerView.Adapter<DBdataAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.db_item_recyclerview,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvRecyclerLogID: TextView = itemView.findViewById(R.id.tv_recycler_logId)
        private val tvRecyclerSensorX: TextView = itemView.findViewById(R.id.tv_recycler_sensorX)
        private val tvRecyclerSensorY: TextView = itemView.findViewById(R.id.tv_recycler_sensorY)
        private val tvRecyclerSensorZ: TextView = itemView.findViewById(R.id.tv_recycler_sensorZ)

        fun bind(item: dbListData) {

            tvRecyclerLogID.text = "Log_ID: " + item.Lod_ID
            tvRecyclerSensorX.text = "X: " + item.Sensor_x
            tvRecyclerSensorY.text = "Y: " + item.Sensor_y
            tvRecyclerSensorZ.text = "Z: " + item.Sensor_z

        }
    }


}