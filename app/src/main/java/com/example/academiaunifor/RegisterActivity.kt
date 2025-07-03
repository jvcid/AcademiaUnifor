package com.example.academiaunifor

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        usernameEditText = findViewById(R.id.editTextTextUsernameRegister)
        emailEditText = findViewById(R.id.editTextTextEmailRegister)
        passwordEditText = findViewById(R.id.editTextTextPasswordRegister)
        registerButton = findViewById(R.id.buttonRegister)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    override fun onStart() {
        super.onStart()

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.length < 6) {
                Toast.makeText(this, "Preencha todos os campos corretamente (senha ≥ 6 caracteres)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verifica se o nome de usuário já existe
            db.collection("usernames").document(username).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        Toast.makeText(this, "Nome de usuário já existe", Toast.LENGTH_SHORT).show()
                    } else {
                        // Cria o usuário com email e senha
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener { authResult ->
                                val uid = authResult.user?.uid ?: return@addOnSuccessListener

                                // Define a role com base no e-mail (você pode mudar isso depois)
                                val role = if (email == "admin@email.com") "admin" else "user"

                                val userData = mapOf(
                                    "username" to username,
                                    "email" to email,
                                    "role" to role
                                )

                                // Salva os dados do usuário no Firestore
                                db.collection("usuarios").document(uid).set(userData)
                                    .addOnSuccessListener {
                                        // Indexa o nome de usuário
                                        db.collection("usernames").document(username).set(mapOf("uid" to uid))
                                            .addOnSuccessListener {
                                                Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
                                                finish() // volta para tela de login
                                            }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Erro ao salvar usuário: ${it.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Erro ao cadastrar: ${it.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao verificar nome de usuário: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
