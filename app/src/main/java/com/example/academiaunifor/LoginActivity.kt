package com.example.academiaunifor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var forgotPasswordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        usernameInput = findViewById(R.id.editTextTextLogin)
        passwordInput = findViewById(R.id.editTextTextPasswordLogin)
        loginButton = findViewById(R.id.buttonLogin)
        registerButton = findViewById(R.id.buttonCadastre)
        forgotPasswordButton = findViewById(R.id.buttonEsqueci)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Busca o UID vinculado ao username
            db.collection("usernames").document(username).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        val uid = doc.getString("uid")
                        if (uid != null) {
                            // Busca o e-mail a partir do UID
                            db.collection("usuarios").document(uid).get()
                                .addOnSuccessListener { userDoc ->
                                    val email = userDoc.getString("email")
                                    if (!email.isNullOrEmpty()) {
                                        auth.signInWithEmailAndPassword(email, password)
                                            .addOnSuccessListener {
                                                // Login OK
                                                val intent = Intent(this, InicioActivity::class.java)
                                                intent.putExtra("username", username)
                                                startActivity(intent)
                                                finish()
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(this, "Senha incorreta", Toast.LENGTH_SHORT).show()
                                            }
                                    } else {
                                        Toast.makeText(this, "Erro ao obter e-mail", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "Usuário inválido", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao buscar usuário: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        forgotPasswordButton.setOnClickListener {
            startActivity(Intent(this, EsqueciActivity::class.java))
        }
    }
}
