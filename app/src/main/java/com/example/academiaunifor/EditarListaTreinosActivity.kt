package com.example.academiaunifor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class EditarListaTreinosActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GrupoAdapter
    private val listaGrupos = mutableListOf<GrupoMuscular>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_lista_treinos)

        recyclerView = findViewById(R.id.recyclerViewTreinos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = GrupoAdapter(listaGrupos) { grupo, pos ->
            val intent = Intent(this, EditarTreinoActivity::class.java)
            intent.putExtra("grupo", grupo)
            intent.putExtra("posGrupo", pos)
            intent.putExtra("posExercicio", 0) // inicial, pode editar primeiro exercício por padrão
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        buscarTreinos()
    }

    private fun buscarTreinos() {
        db.collection("treinos").get()
            .addOnSuccessListener { result ->
                listaGrupos.clear()
                for (doc in result) {
                    val grupo = GrupoMuscular(
                        nomeGrupo = doc.getString("grupoMuscular") ?: "Sem nome",
                        exercicios = (doc.get("exercicios") as? List<String>)?.toMutableList() ?: mutableListOf(),
                        idDocumento = doc.id
                    )

                    listaGrupos.add(grupo)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("FIREBASE", "Erro ao buscar treinos: ${e.message}")
            }
    }
}
