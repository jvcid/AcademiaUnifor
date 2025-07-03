package com.example.academiaunifor

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class ProgressaoCargaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progressao_carga)

        val lvGruposMusculares = findViewById<ListView>(R.id.lvGruposMusculares)

        val gruposMusculares = listOf("Peito", "Costas", "Ombros", "Pernas", "Braços")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, gruposMusculares)
        lvGruposMusculares.adapter = adapter

        lvGruposMusculares.setOnItemClickListener { _, _, position, _ ->
            val grupoSelecionado = gruposMusculares[position]
            val intent = Intent(this, EscolherExercicioActivity::class.java)
            intent.putExtra("grupoMuscular", grupoSelecionado)
            startActivity(intent)
        }
    }
}
