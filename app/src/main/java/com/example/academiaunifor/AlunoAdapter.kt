package com.example.academiaunifor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlunoAdapter(
    private val alunos: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<AlunoAdapter.AlunoViewHolder>() {

    inner class AlunoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeAluno: TextView = itemView.findViewById(R.id.nomeAluno)

        init {
            itemView.setOnClickListener {
                onClick(alunos[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlunoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_aluno, parent, false)
        return AlunoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlunoViewHolder, position: Int) {
        holder.nomeAluno.text = alunos[position]
    }

    override fun getItemCount(): Int = alunos.size
}
