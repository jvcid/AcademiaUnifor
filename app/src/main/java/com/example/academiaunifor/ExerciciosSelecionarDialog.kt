package com.example.academiaunifor

import android.app.AlertDialog
import android.content.Context

class ExercicioSelecionarDialog(
    context: Context,
    grupo: GrupoMuscular,
    onItemClick: (Int) -> Unit
) {
    private val dialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Escolha um exercício")
        val nomes = grupo.exercicios.toTypedArray()
        builder.setItems(nomes) { _, which ->
            onItemClick(which)
        }
        builder.setNegativeButton("Cancelar", null)
        dialog = builder.create()
    }

    fun show() {
        dialog.show()
    }
}
