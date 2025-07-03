package com.example.academiaunifor

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AlimentacaoUsuarioActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlimentacaoAdapter
    private val listaAlimentos = mutableListOf<Alimento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alimentacao_usuario)

        recyclerView = findViewById(R.id.recyclerViewAlimentacao)
        adapter = AlimentacaoAdapter(listaAlimentos) { /* remover desativado para usuário */ }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        carregarAlimentosDoUsuarioAtual()
    }

    private fun carregarAlimentosDoUsuarioAtual() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios")
            .document(userId)
            .collection("alimentacao")
            .get()
            .addOnSuccessListener { resultado ->
                listaAlimentos.clear()
                for (documento in resultado) {
                    val nome = documento.getString("nome") ?: ""
                    val calorias = documento.getLong("calorias")?.toInt() ?: 0
                    listaAlimentos.add(Alimento(nome, calorias))
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar alimentos", Toast.LENGTH_SHORT).show()
            }
    }
}
