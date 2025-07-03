package com.example.academiaunifor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ConsultaActivity : AppCompatActivity() {

    private lateinit var btnMarcarConsulta: Button
    private lateinit var btnVerConsultas: Button
    private lateinit var spinnerProfessores: Spinner
    private var selectedProfessorId: String = ""
    private var selectedProfessorName: String = ""

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val professores = mutableListOf<Pair<String, String>>() // (ID, Nome)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulta)

        btnMarcarConsulta = findViewById(R.id.btnMarcarConsulta)
        btnVerConsultas = findViewById(R.id.btnVerConsultas)
        spinnerProfessores = findViewById(R.id.spinnerProfessores)

        carregarProfessores()

        btnMarcarConsulta.setOnClickListener {
            if (selectedProfessorId.isNotEmpty()) {
                val intent = Intent(this, CalendarioActivity::class.java).apply {
                    putExtra("professorId", selectedProfessorId)
                    putExtra("professorName", selectedProfessorName)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Selecione um professor", Toast.LENGTH_SHORT).show()
            }
        }

        btnVerConsultas.setOnClickListener {
            startActivity(Intent(this, VerConsultasActivity::class.java))
        }
    }

    private fun carregarProfessores() {
        val progressBar = ProgressBar(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            }
        }
        findViewById<ConstraintLayout>(R.id.constraintLayout).addView(progressBar)

        db.collection("usuarios")
            .whereEqualTo("role", "admin")
            .get()
            .addOnSuccessListener { result ->
                professores.clear()
                val nomes = mutableListOf<String>()
                for (doc in result) {
                    val id = doc.id
                    val nome = doc.getString("username") ?: "Professor"
                    professores.add(Pair(id, nome))
                    nomes.add(nome)
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nomes)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerProfessores.adapter = adapter

                spinnerProfessores.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (professores.isNotEmpty()) {
                            selectedProfessorId = professores[position].first
                            selectedProfessorName = professores[position].second
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        selectedProfessorId = ""
                        selectedProfessorName = ""
                    }
                }
                progressBar.visibility = View.GONE
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao carregar professores: ${e.message}", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
    }
}