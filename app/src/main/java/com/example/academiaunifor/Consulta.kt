package com.example.academiaunifor

data class Consulta(
    var id: String = "",
    val alunoId: String = "",
    val professorId: String = "",
    val data: String = "",
    val horario: String = "",
    var idDocumento: String? = null  // id do documento Firestore para remoção

)
