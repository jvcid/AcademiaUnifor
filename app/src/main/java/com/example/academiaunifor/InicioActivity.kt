package com.example.academiaunifor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InicioActivity : AppCompatActivity() {

    lateinit var welcomeText: TextView
    lateinit var btnIa: Button
    lateinit var btnAlimentacao: Button
    lateinit var btnConsulta: Button
    lateinit var btnColegas: Button
    lateinit var btnNoticias: Button
    lateinit var btnTreinos: Button
    lateinit var containerColegas: LinearLayout
    lateinit var ultimaNoticiaTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio)

        welcomeText = findViewById(R.id.textViewInicio)
        btnIa = findViewById(R.id.buttonIa)
        btnAlimentacao = findViewById(R.id.buttonAlimentacao)
        btnConsulta = findViewById(R.id.buttonConsulta)
        btnColegas = findViewById(R.id.buttonColegas)
        btnNoticias = findViewById(R.id.buttonNoticias)
        btnTreinos = findViewById(R.id.buttonTreinos)
        containerColegas = findViewById(R.id.containerColegas)
        ultimaNoticiaTextView = findViewById(R.id.textViewUltimaNoticia)


        // Ajuste automático para barra de status
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnTreinos.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                FirebaseFirestore.getInstance().collection("usuarios").document(uid).get()
                    .addOnSuccessListener { document ->
                        val role = document.getString("role")
                        when (role) {
                            "admin" -> startActivity(Intent(this, MeusTreinosActivity::class.java))
                            "user" -> startActivity(Intent(this, MeusTreinosUsuarioActivity::class.java))
                            else -> Toast.makeText(this, "Tipo de usuário inválido: $role", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erro ao verificar tipo de usuário", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            }
        }

        btnAlimentacao.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                FirebaseFirestore.getInstance().collection("usuarios").document(uid).get()
                    .addOnSuccessListener { document ->
                        val role = document.getString("role")
                        when (role) {
                            "admin" -> startActivity(Intent(this, AlimentacaoActivity::class.java))
                            "user" -> startActivity(Intent(this, AlimentacaoUsuarioActivity::class.java))
                            else -> Toast.makeText(this, "Tipo de usuário inválido: $role", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erro ao verificar tipo de usuário", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            }
        }

        btnIa.setOnClickListener {
            val intent = Intent(this, IAActivity::class.java)
            startActivity(intent)
        }

        btnColegas.setOnClickListener {
            val intent = Intent(this, ColegasActivity::class.java)
            startActivity(intent)
        }

        btnConsulta.setOnClickListener {
            val intent = Intent(this, ConsultaActivity::class.java)
            startActivity(intent)
        }

        btnNoticias.setOnClickListener {
            val intent = Intent(this, FeedNoticiasActivity::class.java)
            startActivity(intent)
        }

        carregarColegasParaTelaInicial()
        carregarUltimaNoticia()
    }

    override fun onStart() {
        super.onStart()
        val username = intent.getStringExtra("username")
        welcomeText.text = "Bem-vindo, $username!"
    }

    private fun carregarColegasParaTelaInicial() {
        FirebaseFirestore.getInstance().collection("usuarios")
            .whereEqualTo("role", "user")
            .get()
            .addOnSuccessListener { result ->
                containerColegas.removeAllViews()
                for (doc in result) {
                    val iconUrl = doc.getString("iconUrl")
                    val usuario = Usuario(
                        id = doc.id,
                        nome = doc.getString("username") ?: "",
                        role = doc.getString("role") ?: "user",
                        iconUrl = iconUrl
                    )
                    adicionarIconeColega(usuario)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar colegas", Toast.LENGTH_SHORT).show()
            }
    }

    private fun adicionarIconeColega(usuario: Usuario) {
        val imageView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(64.dpToPx(), 64.dpToPx()).apply {
                marginEnd = 16.dpToPx()
            }
            setPadding(8.dpToPx(), 8.dpToPx(), 8.dpToPx(), 8.dpToPx())
            setBackgroundResource(R.drawable.circle_background)
            scaleType = ImageView.ScaleType.CENTER_CROP
            contentDescription = "Ícone de ${usuario.nome}"
        }

        if (!usuario.iconUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(usuario.iconUrl)
                .placeholder(R.drawable.baseline_person_24)
                .circleCrop()
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.baseline_person_24)
        }

        containerColegas.addView(imageView)
    }

    private fun carregarUltimaNoticia() {
        FirebaseFirestore.getInstance().collection("noticias")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    ultimaNoticiaTextView.text = "Erro ao carregar notícia."
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val doc = snapshot.documents[0]
                    val titulo = doc.getString("titulo") ?: "Sem título"
                    ultimaNoticiaTextView.text = titulo
                } else {
                    ultimaNoticiaTextView.text = "Nenhuma notícia publicada ainda."
                }
            }
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}
