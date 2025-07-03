package com.example.academiaunifor

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ConsultaAdminActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val consultas = mutableListOf<Consulta>()
    private lateinit var adapter: ConsultaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulta_admin)

        recyclerView = findViewById(R.id.recyclerViewPacientes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ConsultaAdapter(consultas) { consulta, position ->
            removerConsulta(consulta, position)
        }

        recyclerView.adapter = adapter

        carregarConsultas()
    }

    private fun carregarConsultas() {
        db.collection("consultas")
            .get()
            .addOnSuccessListener { result ->
                consultas.clear()
                val lista = result.mapNotNull { doc ->
                    doc.toObject(Consulta::class.java).apply { id = doc.id }
                }
                consultas.addAll(lista)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar consultas", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removerConsulta(consulta: Consulta, position: Int) {
        db.collection("consultas").document(consulta.id)
            .delete()
            .addOnSuccessListener {
                consultas.removeAt(position)
                adapter.notifyItemRemoved(position)
                Toast.makeText(this, "Consulta removida", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao remover consulta", Toast.LENGTH_SHORT).show()
            }
    }
}
