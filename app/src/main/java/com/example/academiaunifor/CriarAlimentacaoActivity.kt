package com.example.academiaunifor

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent


class CriarAlimentacaoActivity : AppCompatActivity() {

    lateinit var edtNome: EditText
    lateinit var edtCalorias: EditText
    lateinit var btnSalvar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_criar_alimentacao)

        edtNome = findViewById(R.id.edtNomeAlimento)
        edtCalorias = findViewById(R.id.edtCalorias)
        btnSalvar = findViewById(R.id.btnSalvarAlimentacao)

        btnSalvar.setOnClickListener {
            val nome = edtNome.text.toString()
            val caloriasText = edtCalorias.text.toString()

            if (nome.isNotBlank() && caloriasText.isNotBlank()) {
                val alimento = Alimento(nome, caloriasText.toInt())

                // Criar intent com resultado
                val intent = Intent()
                intent.putExtra("novoAlimento", alimento)  // Passa o alimento serializável
                setResult(RESULT_OK, intent)

                Toast.makeText(this, "Salvo: ${alimento.nome} (${alimento.calorias} kcal)", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}