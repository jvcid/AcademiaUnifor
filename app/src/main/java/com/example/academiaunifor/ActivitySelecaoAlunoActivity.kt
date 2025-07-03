
package com.example.academiaunifor
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivitySelecaoAlunoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlunoAdapter
    private val alunos = listOf("Aluno 1", "Aluno 2", "Aluno 3") // Exemplo de alunos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecionar_aluno)

        recyclerView = findViewById(R.id.recyclerViewAlunos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AlunoAdapter(alunos) { alunoId ->
            val intent = Intent(this, EditarTreinoActivity::class.java)
            intent.putExtra("alunoId", alunoId)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }
}
