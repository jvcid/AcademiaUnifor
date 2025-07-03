package com.example.academiaunifor

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class ColegasAdapter(
    private val usuarios: List<Usuario>,
    private val context: Context,
    private val onItemClick: ((Usuario) -> Unit)? = null
) : RecyclerView.Adapter<ColegasAdapter.UsuarioViewHolder>() {

    inner class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconeUsuario: CircleImageView = itemView.findViewById(R.id.imgUsuario)
        val nomeUsuario: TextView = itemView.findViewById(R.id.txtNomeUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]

        if (usuario.isProfessor) {
            holder.iconeUsuario.setImageResource(R.drawable.ic_professor)
            holder.iconeUsuario.borderColor = Color.parseColor("#AB47BC")
        } else {
            holder.iconeUsuario.setImageResource(R.drawable.ic_colega)
            holder.iconeUsuario.borderColor = Color.parseColor("#42A5F5")
        }

        holder.nomeUsuario.text = usuario.nome
        holder.itemView.setOnClickListener { onItemClick?.invoke(usuario) }

        Log.d("ColegasAdapter", "Exibindo usuário: ${usuario.nome}")
    }

    override fun getItemCount(): Int = usuarios.size
}