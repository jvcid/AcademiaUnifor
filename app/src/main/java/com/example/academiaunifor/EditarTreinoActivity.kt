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
import com.google.firebase.firestore.FirebaseFirestore

class EditarTreinoActivity : AppCompatActivity() {

    private lateinit var btnSelecionarUsuario: Button
    private lateinit var recyclerTreinos: RecyclerView
    private lateinit var adapter: TreinoAdapter

    private val listaUsuarios = mutableListOf<Usuario>()
    private val listaTreinos = mutableListOf<Treino>()

    private var usuarioSelecionado: Usuario? = null
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_treino)

        btnSelecionarUsuario = findViewById(R.id.btnSelecionarUsuario)
        recyclerTreinos = findViewById(R.id.recyclerTreinos)

        adapter = TreinoAdapter(listaTreinos) { treinoSelecionado ->
            mostrarDialogEditarExcluir(treinoSelecionado)
        }

        recyclerTreinos.adapter = adapter
        recyclerTreinos.layoutManager = LinearLayoutManager(this)

        btnSelecionarUsuario.setOnClickListener {
            mostrarSelecaoUsuario()
        }

        carregarUsuarios()
    }

    private fun carregarUsuarios() {
        db.collection("usuarios")
            .get()
            .addOnSuccessListener { result ->
                listaUsuarios.clear()
                for (doc in result) {
                    val usuario = Usuario(
                        id = doc.id,
                        nome = doc.getString("username") ?: "Sem nome"
                    )
                    listaUsuarios.add(usuario)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar usuários", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostrarSelecaoUsuario() {
        val nomes = listaUsuarios.map { it.nome }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Selecionar Aluno")
            .setItems(nomes) { _, which ->
                usuarioSelecionado = listaUsuarios[which]
                btnSelecionarUsuario.text = "Aluno: ${usuarioSelecionado?.nome}"
                usuarioSelecionado?.id?.let { carregarTreinosDoUsuario(it) }
            }
            .show()
    }

    private fun carregarTreinosDoUsuario(usuarioId: String) {
        db.collection("usuarios")
            .document(usuarioId)
            .collection("treinos")
            .get()
            .addOnSuccessListener { result ->
                listaTreinos.clear()
                for (doc in result) {
                    val treino = doc.toObject(Treino::class.java)
                    treino.id = doc.id
                    listaTreinos.add(treino)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar treinos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostrarDialogEditarExcluir(treino: Treino) {
        val options = arrayOf("Editar", "Excluir")
        AlertDialog.Builder(this)
            .setTitle("O que deseja fazer?")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> editarTreino(treino)
                    1 -> confirmarExcluirTreino(treino)
                }
            }
            .show()
    }

    private fun editarTreino(treino: Treino) {
        val intent = Intent(this, CriarTreinoActivity::class.java)
        intent.putExtra("treinoId", treino.id)
        intent.putExtra("userId", usuarioSelecionado?.id)
        startActivity(intent)
    }

    private fun confirmarExcluirTreino(treino: Treino) {
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
        val usuarioId = usuarioSelecionado?.id ?: return
        db.collection("usuarios")
            .document(usuarioId)
            .collection("treinos")
            .document(treino.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Treino excluído com sucesso!", Toast.LENGTH_SHORT).show()
                carregarTreinosDoUsuario(usuarioId)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao excluir treino", Toast.LENGTH_SHORT).show()
            }
    }
}
