package com.example.academiaunifor

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CriarTreinoActivity : AppCompatActivity() {

    private lateinit var btnSelecionarAluno: Button
    private var usuarioSelecionado: Usuario? = null
    private val listaUsuarios = mutableListOf<Usuario>()
    private lateinit var editGrupo: EditText
    private lateinit var editExercicio: EditText
    private lateinit var textExercicios: TextView
    private lateinit var btnAdicionar: Button
    private lateinit var btnSalvar: Button

    private val listaExercicios = mutableListOf<String>()
    private val db = FirebaseFirestore.getInstance()
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_treino)

        btnSelecionarAluno = findViewById(R.id.btnSelecionarAluno)
        editGrupo = findViewById(R.id.editGrupo)
        editExercicio = findViewById(R.id.editExercicio)
        textExercicios = findViewById(R.id.textExercicios)
        btnAdicionar = findViewById(R.id.btnAdicionarExercicio)
        btnSalvar = findViewById(R.id.btnSalvarTreino)
        userId = intent.getStringExtra("userId")

        btnAdicionar.setOnClickListener {
            val exercicio = editExercicio.text.toString().trim()
            if (exercicio.isNotEmpty()) {
                listaExercicios.add(exercicio)
                atualizarLista()
                editExercicio.text.clear()
            }
        }

        btnSalvar.setOnClickListener {
            salvarTreino()
        }

        btnSelecionarAluno.setOnClickListener {
            mostrarSelecaoUsuario()
        }
        carregarUsuarios()

    }

    private fun carregarUsuarios() {
        FirebaseFirestore.getInstance().collection("usuarios")
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
    }

    private fun mostrarSelecaoUsuario() {
        val nomes = listaUsuarios.map { it.nome }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Selecionar Aluno")
            .setItems(nomes) { _, which ->
                usuarioSelecionado = listaUsuarios[which]
                btnSelecionarAluno.text = "Aluno: ${usuarioSelecionado?.nome}"
            }
            .show()
    }

    private fun atualizarLista() {
        val texto = "Exercícios:\n" + listaExercicios.joinToString("\n")
        textExercicios.text = texto
    }

    private fun salvarTreino() {
        val grupo = editGrupo.text.toString().trim()
        val alunoId = usuarioSelecionado?.id ?: userId

        if (alunoId == null || grupo.isEmpty() || listaExercicios.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        val treinoId = UUID.randomUUID().toString()
        val treino = mapOf(
            "id" to treinoId,
            "grupoMuscular" to grupo,
            "exercicios" to listaExercicios
        )

        db.collection("usuarios")
            .document(alunoId)
            .collection("treinos")
            .document(treinoId)
            .set(treino)
            .addOnSuccessListener {
                Toast.makeText(this, "Treino salvo com sucesso!", Toast.LENGTH_SHORT).show()
                // Retorna para a activity anterior passando o userId
                val intent = Intent()
                intent.putExtra("userId", alunoId)
                setResult(RESULT_OK, intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao salvar treino!", Toast.LENGTH_SHORT).show()
            }
    }

}