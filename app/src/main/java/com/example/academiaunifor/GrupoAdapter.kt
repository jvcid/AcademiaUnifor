package com.example.academiaunifor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GrupoAdapter(
    private val grupos: List<GrupoMuscular>,
    private val onExercicioClick: (GrupoMuscular, Int) -> Unit
) : RecyclerView.Adapter<GrupoAdapter.GrupoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrupoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grupo_muscular, parent, false)
        return GrupoViewHolder(view)
    }

    override fun onBindViewHolder(holder: GrupoViewHolder, position: Int) {
        val grupo = grupos[position]
        holder.bind(grupo)
    }

    override fun getItemCount() = grupos.size

    inner class GrupoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tituloGrupo: TextView = itemView.findViewById(R.id.txtGrupo)

        fun bind(grupo: GrupoMuscular) {
            tituloGrupo.text = grupo.nomeGrupo
            itemView.setOnClickListener {
                ExercicioSelecionarDialog(itemView.context, grupo) { index ->
                    onExercicioClick(grupo, index)
                }.show()
            }
        }
    }
}
