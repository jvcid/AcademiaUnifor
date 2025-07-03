package com.example.academiaunifor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.academiaunifor.databinding.FragmentUsuariosBinding
import com.google.firebase.firestore.FirebaseFirestore

class UsuariosFragment : Fragment() {

    private var _binding: FragmentUsuariosBinding? = null
    private val binding get() = _binding!!

    private lateinit var professoresAdapter: UsuarioAdapter
    private lateinit var colegasAdapter: UsuarioAdapter

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsuariosBinding.inflate(inflater, container, false)

        // Professores
        binding.recyclerProfessores.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        professoresAdapter = UsuarioAdapter(listOf())
        binding.recyclerProfessores.adapter = professoresAdapter

        // Colegas
        binding.recyclerColegas.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        colegasAdapter = UsuarioAdapter(listOf())
        binding.recyclerColegas.adapter = colegasAdapter

        carregarUsuariosFirebase()

        return binding.root
    }

    private fun carregarUsuariosFirebase() {
        db.collection("usuarios")
            .get()
            .addOnSuccessListener { result ->
                val professores = mutableListOf<Usuario>()
                val colegas = mutableListOf<Usuario>()

                for (document in result) {
                    val usuario = document.toObject(Usuario::class.java)
                    if (usuario.isProfessor) {
                        professores.add(usuario)
                    } else {
                        colegas.add(usuario)
                    }
                }

                professoresAdapter.atualizarUsuarios(professores)
                colegasAdapter.atualizarUsuarios(colegas)
            }
            .addOnFailureListener {
                // Erro na leitura do Firestore
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
