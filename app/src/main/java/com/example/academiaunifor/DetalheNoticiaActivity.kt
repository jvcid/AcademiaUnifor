package com.example.academiaunifor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.academiaunifor.databinding.ActivityDetalheNoticiaBinding
import java.text.SimpleDateFormat
import java.util.*

class DetalheNoticiaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalheNoticiaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalheNoticiaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titulo = intent.getStringExtra("titulo")
        val autor = intent.getStringExtra("autor")
        val texto = intent.getStringExtra("texto")
        val dataMillis = intent.getLongExtra("data", 0L)

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dataFormatada = sdf.format(Date(dataMillis))

        binding.txtTitulo.text = titulo
        binding.txtAutor.text = autor
        binding.txtData.text = dataFormatada
        binding.txtTextoCompleto.text = texto
    }
}