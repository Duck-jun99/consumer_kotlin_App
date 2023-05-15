package com.kotlinstudy.consumer_kotlin_app.Network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Datamodel(

    @SerializedName("log_ID")
    @Expose
    var logId: String?,

    @SerializedName("Sensor_x")
    @Expose
    var sensorX: String?,

    @SerializedName("Sensor_y")
    @Expose
    var sensorY: String?,

    @SerializedName("Sensor_z")
    @Expose
    var sensorZ: String?
)


data class GetDatamodel(
    val getDatamodel: List<Datamodel>
)

data class LogResponse(
    val logs: List<Datamodel>
)