package com.example.academiaunifor

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AlimentacaoActivity : AppCompatActivity() {

    private var listaUsuarios = mutableListOf<Usuario>()
    private var usuarioSelecionado: Usuario? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlimentacaoAdapter
    private val listaAlimentos = mutableListOf<Alimento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alimentacao)

        recyclerView = findViewById(R.id.recyclerViewAlimentacao)
        adapter = AlimentacaoAdapter(listaAlimentos) { posicao ->
            confirmarRemocao(posicao)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val btnAdicionar: Button = findViewById(R.id.btnAdicionarAlimento)
        btnAdicionar.setOnClickListener {
            mostrarDialogoSelecionarUsuario()
        }

        carregarUsuarios()
    }

    private fun mostrarDialogoSelecionarUsuario() {
        val nomes = listaUsuarios.map { it.nome }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Selecionar Usuário")
            .setItems(nomes) { _, which ->
                usuarioSelecionado = listaUsuarios[which]
                Toast.makeText(this, "Usuário selecionado: ${usuarioSelecionado?.nome}", Toast.LENGTH_SHORT).show()

                // Carrega alimentos do usuário selecionado
                carregarAlimentosDoUsuario(usuarioSelecionado!!)

                // Depois mostra diálogo para adicionar alimento
                mostrarDialogoAdicionarAlimento()
            }
            .setCancelable(false)
            .show()
    }

    private fun mostrarDialogoAdicionarAlimento() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val inputNome = EditText(this).apply {
            hint = "Nome do alimento"
        }
        val inputCalorias = EditText(this).apply {
            hint = "Calorias"
            inputType = InputType.TYPE_CLASS_NUMBER
        }

        layout.addView(inputNome)
        layout.addView(inputCalorias)

        AlertDialog.Builder(this)
            .setTitle("Adicionar Alimentação para ${usuarioSelecionado?.nome}")
            .setView(layout)
            .setPositiveButton("Adicionar") { _, _ ->
                val nome = inputNome.text.toString()
                val calorias = inputCalorias.text.toString().toIntOrNull() ?: 0

                if (nome.isNotBlank()) {
                    val alimento = Alimento(nome, calorias)

                    val db = FirebaseFirestore.getInstance()

                    if (usuarioSelecionado != null) {
                        db.collection("usuarios")
                            .document(usuarioSelecionado!!.id)
                            .collection("alimentacao")
                            .add(alimento)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Alimento salvo para ${usuarioSelecionado!!.nome}", Toast.LENGTH_SHORT).show()
                                // Atualiza lista local e adapter
                                listaAlimentos.add(alimento)
                                adapter.notifyItemInserted(listaAlimentos.size - 1)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Erro ao salvar: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Nenhum usuário selecionado.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Preencha o nome!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmarRemocao(posicao: Int) {
        AlertDialog.Builder(this)
            .setTitle("Remover item")
            .setMessage("Deseja remover este alimento da lista?")
            .setPositiveButton("Sim") { _, _ ->
                val alimentoRemovido = listaAlimentos[posicao]

                // Remove do Firestore também
                if (usuarioSelecionado != null) {
                    val db = FirebaseFirestore.getInstance()
                    // Busca o documento para remover pelo nome e calorias (não ideal, mas como exemplo)
                    db.collection("usuarios")
                        .document(usuarioSelecionado!!.id)
                        .collection("alimentacao")
                        .whereEqualTo("nome", alimentoRemovido.nome)
                        .whereEqualTo("calorias", alimentoRemovido.calorias)
                        .get()
                        .addOnSuccessListener { resultado ->
                            for (doc in resultado.documents) {
                                doc.reference.delete()
                            }
                        }
                }

                listaAlimentos.removeAt(posicao)
                adapter.notifyItemRemoved(posicao)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun carregarUsuarios() {
        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios")
            .get()
            .addOnSuccessListener { resultado ->
                listaUsuarios.clear()
                for (documento in resultado) {
                    val usuario = Usuario(
                        id = documento.id,
                        nome = documento.getString("username") ?: "Sem nome"
                    )
                    listaUsuarios.add(usuario)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar usuários.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun carregarAlimentosDoUsuario(usuario: Usuario) {
        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios")
            .document(usuario.id)
            .collection("alimentacao")
            .get()
            .addOnSuccessListener { resultado ->
                listaAlimentos.clear()
                for (documento in resultado) {
                    val nome = documento.getString("nome") ?: ""
                    val calorias = documento.getLong("calorias")?.toInt() ?: 0
                    val alimento = Alimento(nome, calorias)
                    listaAlimentos.add(alimento)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar alimentos.", Toast.LENGTH_SHORT).show()
            }
    }

}