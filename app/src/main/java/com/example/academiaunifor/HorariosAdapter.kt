package com.example.academiaunifor

import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HorariosAdapter(
    private val horarios: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<HorariosAdapter.HorarioViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class HorarioViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView) {
        fun bind(horario: String, isSelected: Boolean) {
            textView.text = horario
            textView.setBackgroundColor(if (isSelected) Color.parseColor("#2D3A87") else Color.TRANSPARENT)
            textView.setTextColor(if (isSelected) Color.WHITE else Color.BLACK)
            textView.setOnClickListener {
                val oldPos = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(oldPos)
                notifyItemChanged(selectedPosition)
                onItemClick(horario)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
        val textView = TextView(parent.context).apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16
            }
            setPadding(24, 24, 24, 24)
            textSize = 18f
            setBackgroundResource(android.R.drawable.btn_default)
        }
        return HorarioViewHolder(textView)
    }

    override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
        holder.bind(horarios[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = horarios.size
}
