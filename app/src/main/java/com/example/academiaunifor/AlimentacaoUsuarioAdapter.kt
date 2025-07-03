package com.example.academiaunifor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlimentacaoUsuarioAdapter(private val alimentos:List<Alimento>) : RecyclerView.Adapter<AlimentacaoUsuarioAdapter.AlimentacaoViewHolder>() {

    class AlimentacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTextView: TextView = itemView.findViewById(R.id.textNome)
        val caloriasTextView: TextView = itemView.findViewById(R.id.textCalorias)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlimentacaoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alimento, parent, false)
        return AlimentacaoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlimentacaoViewHolder, position: Int) {
        val alimento = alimentos[position]
        holder.nomeTextView.text = alimento.nome
        holder.caloriasTextView.text = "${alimento.calorias} kcal"
    }

    override fun getItemCount(): Int = alimentos.size
}
