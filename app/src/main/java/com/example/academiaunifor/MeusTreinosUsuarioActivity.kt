package com.example.academiaunifor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academiaunifor.model.Treino
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MeusTreinosUsuarioActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TreinoAdapter
    private val listaTreinos = mutableListOf<Treino>()
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meus_treinos_usuario)

        recyclerView = findViewById(R.id.recyclerMeusTreinosUsuario)
        recyclerView.layoutManager = LinearLayoutManager(this)

        uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        adapter = TreinoAdapter(listaTreinos) { treinoSelecionado ->
            // Aqui pode deixar vazio ou colocar ação de clique se quiser
        }
        recyclerView.adapter = adapter

        carregarTreinosDoUsuario()

        val btnProgressaoCarga = findViewById<Button>(R.id.btnProgressaoCarga)
        btnProgressaoCarga.setOnClickListener {
            startActivity(Intent(this, ProgressaoCargaActivity::class.java))
        }
    }

    private fun carregarTreinosDoUsuario() {
        val db = FirebaseFirestore.getInstance()

        if (uid.isNotEmpty()) {
            val treinosRef = db.collection("usuarios")
                .document(uid)
                .collection("treinos")

            treinosRef.get()
                .addOnSuccessListener { result ->
                    val novaLista = mutableListOf<Treino>()
                    for (document in result) {
                        val treino = document.toObject(Treino::class.java)
                        novaLista.add(treino)
                    }
                    adapter.atualizarLista(novaLista)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao buscar treinos: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
        }
    }
}