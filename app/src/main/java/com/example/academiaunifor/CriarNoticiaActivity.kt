package com.example.academiaunifor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.academiaunifor.databinding.ActivityCriarNoticiaBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class CriarNoticiaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCriarNoticiaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCriarNoticiaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSalvar.setOnClickListener {
            val titulo = binding.edtTitulo.text.toString().trim()
            val autor = binding.edtAutor.text.toString().trim()
            val texto = binding.edtTexto.text.toString().trim()

            if (titulo.isEmpty() || autor.isEmpty() || texto.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val noticia = hashMapOf(
                "titulo" to titulo,
                "autor" to autor,
                "texto" to texto,
                "timestamp" to FieldValue.serverTimestamp()
            )

            FirebaseFirestore.getInstance().collection("noticias")
                .add(noticia)
                .addOnSuccessListener {
                    Toast.makeText(this, "Notícia criada com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao criar notícia", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
