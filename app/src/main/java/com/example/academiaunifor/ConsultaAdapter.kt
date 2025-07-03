package com.example.academiaunifor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ConsultaAdapter(
    private val consultas: MutableList<Consulta>,
    private val onDeleteClick: (Consulta, Int) -> Unit
) : RecyclerView.Adapter<ConsultaAdapter.ConsultaViewHolder>() {

    inner class ConsultaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvData: TextView = view.findViewById(R.id.tvData)
        val tvHorario: TextView = view.findViewById(R.id.tvHorario)
        val btnRemover: Button = view.findViewById(R.id.btnRemover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_consulta, parent, false)
        return ConsultaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConsultaViewHolder, position: Int) {
        val consulta = consultas[position]
        holder.tvData.text = consulta.data
        holder.tvHorario.text = consulta.horario

        holder.btnRemover.setOnClickListener {
            onDeleteClick(consulta, position)
        }
    }

    override fun getItemCount() = consultas.size
}
