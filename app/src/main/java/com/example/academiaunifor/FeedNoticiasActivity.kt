package com.example.academiaunifor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.academiaunifor.databinding.ActivityFeedNoticiasBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class FeedNoticiasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedNoticiasBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var newsAdapter: NewsAdapter
    private var newsListener: ListenerRegistration? = null
    private var isAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedNoticiasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUserRole()
        binding.fabCriarNoticia.setOnClickListener {
            startActivity(Intent(this, CriarNoticiaActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(
            onItemClick = { openNewsDetail(it) },
            showDelete = isAdmin,
            onDeleteClick = { news -> confirmDeleteNews(news) }
        )
        binding.recyclerViewNews.apply {
            layoutManager = LinearLayoutManager(this@FeedNoticiasActivity)
            adapter = newsAdapter
        }
    }

    private fun confirmDeleteNews(news: News) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar exclusão")
            .setMessage("Tem certeza que deseja excluir esta notícia?")
            .setPositiveButton("Excluir") { _, _ -> deleteNews(news) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteNews(news: News) {
        if (news.id.isEmpty()) {
            Toast.makeText(this, "ID da notícia inválido", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("noticias")
            .document(news.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Notícia excluída com sucesso", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao excluir: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun startListeningForNews() {
        newsListener?.remove()
        newsListener = db.collection("noticias")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                val newsList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(News::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                newsAdapter.submitList(newsList)
            }
    }

    private fun checkUserRole() {
        auth.currentUser?.uid?.let { uid ->
            db.collection("usuarios").document(uid).get()
                .addOnSuccessListener { document ->
                    isAdmin = document.getString("role") == "admin"
                    binding.fabCriarNoticia.visibility = if (isAdmin) View.VISIBLE else View.GONE
                    setupRecyclerView()
                    startListeningForNews()
                }
        }
    }

    private fun openNewsDetail(news: News) {
        val intent = Intent(this, DetalheNoticiaActivity::class.java).apply {
            putExtra("titulo", news.titulo)
            putExtra("autor", news.autor)
            putExtra("texto", news.texto)
            putExtra("data", news.timestamp?.time ?: 0L)
        }
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        newsListener?.remove()
    }
}
