package com.kotlinstudy.consumer_kotlin_app.GuitarModel

import android.widget.ImageView
import kotlin.random.Random

data class GuitarModel(
    val guitar: ImageView,
    val speed: Float = Random.nextInt(2, 10).toFloat()
)
