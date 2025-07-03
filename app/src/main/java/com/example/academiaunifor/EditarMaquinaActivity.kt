package com.example.academiaunifor

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class EditarMaquinaActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var fab: ExtendedFloatingActionButton
    private var listaMaquinas = mutableListOf<Maquina>()
    private lateinit var adapter: MaquinaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_maquina)

        recycler = findViewById(R.id.recyclerMaquinas)
        fab = findViewById(R.id.fabAdicionar)

        adapter = MaquinaAdapter(listaMaquinas) { maquina ->
            AlertDialog.Builder(this)
                .setTitle("Excluir Máquina")
                .setMessage("Deseja excluir ${maquina.nome}?")
                .setPositiveButton("Sim") { _, _ ->
                    FirebaseHelper.excluirMaquina(maquina) { success ->
                        if (success) carregarMaquinas()
                        else Toast.makeText(this, "Erro ao excluir", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Não", null)
                .show()
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        fab.setOnClickListener { mostrarDialogAdicionar() }

        carregarMaquinas()
    }

    private fun carregarMaquinas() {
        FirebaseHelper.listarMaquinas { maquinas ->
            listaMaquinas.clear()
            listaMaquinas.addAll(maquinas)
            adapter.notifyDataSetChanged()
        }
    }

    private fun mostrarDialogAdicionar() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val nome = EditText(this).apply { hint = "Nome da Máquina" }
        val grupo = EditText(this).apply { hint = "Grupo Muscular" }

        layout.addView(nome)
        layout.addView(grupo)

        AlertDialog.Builder(this)
            .setTitle("Nova Máquina")
            .setView(layout)
            .setPositiveButton("Salvar") { _, _ ->
                val nomeStr = nome.text.toString().trim()
                val grupoStr = grupo.text.toString().trim()
                if (nomeStr.isNotEmpty() && grupoStr.isNotEmpty()) {
                    FirebaseHelper.adicionarMaquina(Maquina(nomeStr, grupoStr)) { success ->
                        if (success) carregarMaquinas()
                        else Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
