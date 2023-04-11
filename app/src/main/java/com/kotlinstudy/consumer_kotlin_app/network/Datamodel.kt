package com.kotlinstudy.consumer_kotlin_app.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// * SerializedName: 서버(PHP)에서 안드로이드에 응답할 때 보내는 배열(객체)의 키
//ex) array("Log_ID" => "$Log_ID" , "Sensor_x" => "$Sensor_x" )
// * 위의 PHP 코드에서 "Log_ID" 부분이 serializedName('파라미터값')의 파라미터 값 입니다.

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