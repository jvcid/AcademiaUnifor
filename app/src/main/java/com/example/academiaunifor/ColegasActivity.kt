package com.example.academiaunifor

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ColegasActivity : AppCompatActivity() {

    private lateinit var recyclerProfessores: RecyclerView
    private lateinit var recyclerColegas: RecyclerView
    private lateinit var txtProfessores: TextView
    private lateinit var txtColegas: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colegas)

        recyclerProfessores = findViewById(R.id.recyclerProfessores)
        recyclerColegas = findViewById(R.id.recyclerColegas)
        txtProfessores = findViewById(R.id.txtProfessores)
        txtColegas = findViewById(R.id.txtColegas)

        recyclerProfessores.layoutManager = GridLayoutManager(this, 4)
        recyclerColegas.layoutManager = GridLayoutManager(this, 4)

        carregarUsuarios()
    }

    private fun carregarUsuarios() {
        FirebaseFirestore.getInstance().collection("usuarios")
            .get()
            .addOnSuccessListener { result ->
                val professores = mutableListOf<Usuario>()
                val colegas = mutableListOf<Usuario>()

                for (doc in result) {
                    val nome = doc.getString("username") ?: continue
                    val role = doc.getString("role") ?: "user"

                    val usuario = Usuario(
                        id = doc.id,
                        nome = nome,
                        role = role
                    )

                    if (usuario.isProfessor) professores.add(usuario)
                    else colegas.add(usuario)
                }

                recyclerProfessores.adapter = UsuarioAdapter(professores)
                recyclerColegas.adapter = UsuarioAdapter(colegas)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar colegas", Toast.LENGTH_SHORT).show()
            }
    }
}
