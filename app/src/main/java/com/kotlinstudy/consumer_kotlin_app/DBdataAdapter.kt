package com.kotlinstudy.consumer_kotlin_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DBdataAdapter(private var datas: MutableList<dbListData>) : RecyclerView.Adapter<DBdataAdapter.ViewHolder>() {


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

            tvRecyclerLogID.text = item.Lod_ID
            tvRecyclerSensorX.text = item.Sensor_x
            tvRecyclerSensorY.text = item.Sensor_y
            tvRecyclerSensorZ.text = item.Sensor_z

        }
    }


}