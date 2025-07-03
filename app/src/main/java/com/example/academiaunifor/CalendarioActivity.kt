package com.example.academiaunifor

import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class CalendarioActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var btnConfirmarConsulta: Button
    private lateinit var recyclerViewHorarios: RecyclerView
    private lateinit var adapter: HorariosAdapter

    private var dataSelecionada: String = ""
    private var horarioSelecionado: String? = null
    private lateinit var professorId: String

    private val horariosDisponiveis = listOf("15:00", "15:30", "16:00", "16:30")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        calendarView = findViewById(R.id.calendarView)
        btnConfirmarConsulta = findViewById(R.id.btnConfirmarConsulta)
        recyclerViewHorarios = findViewById(R.id.recyclerViewHorarios)

        professorId = intent.getStringExtra("professorId") ?: ""

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dataSelecionada = sdf.format(calendarView.date)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            dataSelecionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
        }

        adapter = HorariosAdapter(horariosDisponiveis) { horario ->
            horarioSelecionado = horario
        }

        recyclerViewHorarios.layoutManager = LinearLayoutManager(this)
        recyclerViewHorarios.adapter = adapter

        btnConfirmarConsulta.setOnClickListener {
            if (horarioSelecionado == null) {
                Toast.makeText(this, "Por favor, selecione um horário.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val consulta = hashMapOf(
                "data" to dataSelecionada,
                "horario" to horarioSelecionado,
                "professorId" to professorId,
                "alunoId" to FirebaseAuth.getInstance().currentUser?.uid
            )

            FirebaseFirestore.getInstance().collection("consultas")
                .add(consulta)
                .addOnSuccessListener {
                    Toast.makeText(this, "Consulta marcada com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao marcar consulta.", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
