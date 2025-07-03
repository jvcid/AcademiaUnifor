package com.example.academiaunifor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.academiaunifor.databinding.ItemUsuarioBinding

class UsuarioAdapter(
    private var usuarios: List<Usuario>,
    private val onItemClick: ((Usuario) -> Unit)? = null
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    inner class UsuarioViewHolder(private val binding: ItemUsuarioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(usuario: Usuario) {
            binding.txtNomeUsuario.text = usuario.nome

            val icone = if (usuario.isProfessor) {
                R.drawable.ic_professor
            } else {
                R.drawable.ic_colega
            }

            Glide.with(binding.root.context)
                .load(icone)
                .into(binding.imgUsuario)

            binding.root.setOnClickListener {
                onItemClick?.invoke(usuario)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val binding = ItemUsuarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsuarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        holder.bind(usuarios[position])
    }

    override fun getItemCount(): Int = usuarios.size

    fun atualizarUsuarios(novaLista: List<Usuario>) {
        usuarios = novaLista
        notifyDataSetChanged()
    }
}
