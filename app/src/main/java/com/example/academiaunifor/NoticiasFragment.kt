package com.example.academiaunifor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class NoticiasFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val TAG = "NoticiasFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_noticias, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewNoticias)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = NewsAdapter(
            showDelete = true,
            onItemClick = { news -> showNewsDetails(news) },
            onDeleteClick = { news -> confirmDeleteNews(news) }
        )
        recyclerView.adapter = adapter

        fetchNoticias()
        return view
    }

    private fun fetchNoticias() {
        firestore.collection("noticias")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error)
                    Toast.makeText(requireContext(), "Erro ao carregar notícias", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                val listaNoticias = value?.documents?.mapNotNull { doc ->
                    val titulo = doc.getString("titulo") ?: return@mapNotNull null
                    val autor = doc.getString("autor") ?: return@mapNotNull null
                    val texto = doc.getString("texto") ?: ""
                    val timestamp = doc.getTimestamp("timestamp")?.toDate() ?: return@mapNotNull null

                    News(
                        id = doc.id,
                        titulo = titulo,
                        autor = autor,
                        texto = texto,
                        timestamp = timestamp
                    )
                } ?: emptyList()

                adapter.submitList(listaNoticias)
            }
    }

    private fun confirmDeleteNews(news: News) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar exclusão")
            .setMessage("Tem certeza que deseja excluir esta notícia?")
            .setPositiveButton("Excluir") { _, _ ->
                deleteNews(news)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteNews(news: News) {
        if (news.id.isEmpty()) {
            Toast.makeText(requireContext(), "ID da notícia inválido", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Tentando excluir notícia com ID: ${news.id}")

        firestore.collection("noticias")
            .document(news.id)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Notícia excluída com sucesso")
                // Não precisa chamar fetchNoticias() pois o snapshotListener já atualizará
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Erro ao excluir notícia", e)
                Toast.makeText(
                    requireContext(),
                    "Erro ao excluir: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun showNewsDetails(news: News) {
        // Implemente a exibição dos detalhes da notícia se necessário
    }
}