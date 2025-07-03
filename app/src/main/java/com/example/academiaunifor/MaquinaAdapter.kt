package com.example.academiaunifor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MaquinaAdapter(
    private val maquinas: List<Maquina>,
    private val onLongClick: (Maquina) -> Unit
) : RecyclerView.Adapter<MaquinaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome = view.findViewById<TextView>(R.id.txtNome)
        val grupo = view.findViewById<TextView>(R.id.txtGrupo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_maquina, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = maquinas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val maquina = maquinas[position]
        holder.nome.text = maquina.nome
        holder.grupo.text = maquina.grupo

        holder.itemView.setOnLongClickListener {
            onLongClick(maquina)
            true
        }
    }
}
