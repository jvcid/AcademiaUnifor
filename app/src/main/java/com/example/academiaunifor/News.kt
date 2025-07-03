package com.example.academiaunifor

import java.util.*

data class News(
    val id: String = "",
    val titulo: String = "",
    val autor: String = "",
    val texto: String = "",
    val timestamp: Date? = null
)
