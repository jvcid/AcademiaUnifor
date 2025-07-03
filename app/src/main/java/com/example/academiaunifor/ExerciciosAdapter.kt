package com.example.academiaunifor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academiaunifor.databinding.ItemGrupoMuscularBinding

class ExerciciosAdapter(
    private val context: Context,
    private val listaGrupos: List<GrupoMuscular>,
    private val onEditarClick: (grupo: GrupoMuscular, posGrupo: Int, posExercicio: Int) -> Unit
) : RecyclerView.Adapter<ExerciciosAdapter.GrupoViewHolder>() {

    inner class GrupoViewHolder(val binding: ItemGrupoMuscularBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrupoViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemGrupoMuscularBinding.inflate(layoutInflater, parent, false)
        return GrupoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GrupoViewHolder, position: Int) {
        val grupo = listaGrupos[position]
        val binding = holder.binding

        binding.txtGrupoNome.text = grupo.nomeGrupo

        binding.listaExercicios.removeAllViews()
        grupo.exercicios.forEachIndexed { index, exercicio ->
            val itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_exercicio, binding.listaExercicios, false) as ViewGroup

            val nomeView = itemView.findViewById<android.widget.TextView>(R.id.txtExercicioNome)
            val botaoEditar = itemView.findViewById<android.widget.Button>(R.id.btnEditar)

            nomeView.text = exercicio
            botaoEditar.setOnClickListener {
                onEditarClick(grupo, position, index)
            }

            binding.listaExercicios.addView(itemView)
        }
    }

    override fun getItemCount(): Int = listaGrupos.size
}
