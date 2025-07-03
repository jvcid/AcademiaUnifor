package com.example.academiaunifor

data class Usuario(
    var id: String = "",
    val nome: String = "",
    val role: String = "",
    val iconUrl: String? = null
) {
    val isProfessor: Boolean
        get() = role == "admin"
}
