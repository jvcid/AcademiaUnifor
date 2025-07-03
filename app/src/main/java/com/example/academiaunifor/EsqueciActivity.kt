package com.example.academiaunifor

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class EsqueciActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var emailEsqueci: EditText
    lateinit var usuarioEsqueci: EditText
    lateinit var btnEsqueci: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_esqueci)

        btnEsqueci = findViewById(R.id.buttonEsqueciB)
        emailEsqueci = findViewById(R.id.editTextTextEmailEsqueci)
        usuarioEsqueci = findViewById(R.id.editTextTextUserEsqueci)

        auth = FirebaseAuth.getInstance()

    }

    override fun onStart() {
        super.onStart()

        btnEsqueci.setOnClickListener {
            //não precisa do usuario, falar com o cid sobre && remover do template
            val email = emailEsqueci.text.toString()
            val usuario = usuarioEsqueci.text.toString()

            if (email.isNotEmpty() && usuario.isNotEmpty()) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Email enviado!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Preencha o e-mail", Toast.LENGTH_SHORT).show()
            }
        }
    }
}