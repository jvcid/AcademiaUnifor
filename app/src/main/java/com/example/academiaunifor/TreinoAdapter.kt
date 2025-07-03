package com.example.academiaunifor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.academiaunifor.R
import com.example.academiaunifor.model.Treino

class TreinoAdapter(
    private val treinos: MutableList<Treino>,
    private val onItemClick: (Treino) -> Unit
) : RecyclerView.Adapter<TreinoAdapter.TreinoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreinoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_treino, parent, false)
        return TreinoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TreinoViewHolder, position: Int) {
        val treino = treinos[position]
        holder.bind(treino)
        holder.itemView.setOnClickListener {
            onItemClick(treino)
        }
    }

    override fun getItemCount(): Int = treinos.size

    fun atualizarLista(novaLista: List<Treino>) {
        treinos.clear()
        treinos.addAll(novaLista)
        notifyDataSetChanged()
    }

    class TreinoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val grupoMuscularText: TextView = itemView.findViewById(R.id.txtGrupoMuscular)
        private val exerciciosText: TextView = itemView.findViewById(R.id.txtExercicios)

        fun bind(treino: Treino) {
            grupoMuscularText.text = "Grupo: ${treino.grupoMuscular}"
            exerciciosText.text = "Exercícios: ${treino.exercicios.joinToString(", ")}"
        }
    }
}

