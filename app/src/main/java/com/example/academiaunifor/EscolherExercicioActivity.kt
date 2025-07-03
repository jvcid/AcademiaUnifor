package com.example.academiaunifor

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class EscolherExercicioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escolher_exercicio)

        val lvExercicios = findViewById<ListView>(R.id.lvExercicios)

        val grupoMuscular = intent.getStringExtra("grupoMuscular")

        // Lista de exercícios por grupo muscular (você pode ajustar conforme necessário)
        val exercicios = when (grupoMuscular) {
            "Peito" -> listOf("Supino Reto", "Supino Inclinado", "Crucifixo")
            "Costas" -> listOf("Puxada na Barra", "Remada Curvada", "Puxada Frente")
            "Ombros" -> listOf("Desenvolvimento", "Elevação Lateral", "Elevação Frontal")
            "Pernas" -> listOf("Agachamento", "Leg Press", "Cadeira Extensora")
            "Braços" -> listOf("Rosca Direta", "Rosca Martelo", "Tríceps Testa")
            else -> listOf()
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, exercicios)
        lvExercicios.adapter = adapter

        lvExercicios.setOnItemClickListener { _, _, position, _ ->
            val exercicioSelecionado = exercicios[position]
            val intent = Intent(this, AlteracaoCargaActivity::class.java)
            intent.putExtra("exercicio", exercicioSelecionado)
            startActivity(intent)
        }
    }
}
