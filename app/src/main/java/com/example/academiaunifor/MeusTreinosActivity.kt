package com.example.academiaunifor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academiaunifor.model.Treino
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MeusTreinosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TreinoAdapter
    private val listaTreinos = mutableListOf<Treino>()
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meus_treinos)

        recyclerView = findViewById(R.id.recyclerTreinos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        adapter = TreinoAdapter(listaTreinos) { treinoSelecionado ->
            mostrarDialogExcluir(treinoSelecionado)
        }
        recyclerView.adapter = adapter

        carregarTreinosDoUsuario()

        // seus botões e listeners continuam iguais (ex: btnProgressaoCarga, btnAddTreino, btnMaquinas)
        val btnProgressaoCarga = findViewById<Button>(R.id.btnProgressaoCarga)
        btnProgressaoCarga.setOnClickListener {
            startActivity(Intent(this, ProgressaoCargaActivity::class.java))
        }

        val addButton = findViewById<Button>(R.id.btnAddTreino)
        addButton.setOnClickListener {
            showTreinoOptionsDialog()
        }

        val btnMaquinas = findViewById<Button>(R.id.btnMaquinas)
        btnMaquinas.setOnClickListener {
            startActivity(Intent(this, EditarMaquinaActivity::class.java))
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
    private fun mostrarDialogExcluir(treino: Treino) {
        AlertDialog.Builder(this)
            .setTitle("Excluir treino")
            .setMessage("Tem certeza que deseja excluir esse treino?")
            .setPositiveButton("Excluir") { _, _ ->
                excluirTreino(treino)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun excluirTreino(treino: Treino) {
        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios")
            .document(uid)
            .collection("treinos")
            .document(treino.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Treino excluído", Toast.LENGTH_SHORT).show()
                carregarTreinosDoUsuario()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao excluir treino", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showTreinoOptionsDialog() {
        val options = arrayOf("Criar Treino", "Editar Treino")
        AlertDialog.Builder(this)
            .setTitle("Escolha uma opção")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> startActivity(Intent(this, CriarTreinoActivity::class.java))
                    1 -> startActivity(Intent(this, EditarTreinoActivity::class.java))
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}


