package com.example.academiaunifor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.academiaunifor.model.Paciente

class PacienteAdapter(
    private val pacientes: List<Paciente>,
    private val onItemClick: ((Paciente) -> Unit)? = null
) : RecyclerView.Adapter<PacienteAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nome: TextView = itemView.findViewById(R.id.textNomePaciente)
        private val data: TextView = itemView.findViewById(R.id.textDataConsulta)
        private val hora: TextView = itemView.findViewById(R.id.textHoraConsulta)

        fun bind(paciente: Paciente) {
            nome.text = paciente.nome ?: "Nome não disponível"
            data.text = "Data: ${paciente.data ?: "N/A"}"
            hora.text = "Hora: ${paciente.hora ?: "N/A"}"

            itemView.setOnClickListener {
                onItemClick?.invoke(paciente)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_paciente, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pacientes[position])
    }

    override fun getItemCount(): Int = pacientes.size
}
