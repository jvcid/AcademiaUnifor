package com.example.academiaunifor

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseHelper {

    private val firestore = FirebaseFirestore.getInstance()

    // Referência para máquinas
    private val maquinasRef = firestore.collection("google")
        .document("maquinas")
        .collection("yfnlk9pXcazjjKtQqzBZ")

    // Referência para consultas
    private val consultasRef = firestore.collection("consultas")

    // Referência para usuários (alunos)
    private val usuariosRef = firestore.collection("user")

    // -------- Funções máquinas --------

    fun adicionarMaquina(maquina: Maquina, cb: (Boolean) -> Unit) {
        maquinasRef.add(maquina)
            .addOnSuccessListener { cb(true) }
            .addOnFailureListener { e ->
                e.printStackTrace()
                cb(false)
            }
    }

    fun excluirMaquina(maquina: Maquina, cb: (Boolean) -> Unit) {
        maquinasRef.whereEqualTo("nome", maquina.nome)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) maquinasRef.document(doc.id).delete()
                cb(true)
            }
            .addOnFailureListener {
                it.printStackTrace()
                cb(false)
            }
    }

    fun listarMaquinas(cb: (List<Maquina>) -> Unit) {
        maquinasRef.get()
            .addOnSuccessListener { result ->
                cb(result.mapNotNull { it.toObject(Maquina::class.java) })
            }
            .addOnFailureListener {
                it.printStackTrace()
                cb(emptyList())
            }
    }

    // -------- Função consultas com nome do aluno --------

    fun listarConsultasComNomeAluno(cb: (List<ConsultaComNomeAluno>) -> Unit) {
        consultasRef.get()
            .addOnSuccessListener { consultasSnapshot ->
                val consultasList = mutableListOf<ConsultaComNomeAluno>()
                val totalConsultas = consultasSnapshot.size()
                var consultasProcessadas = 0

                if (totalConsultas == 0) {
                    cb(emptyList())
                    return@addOnSuccessListener
                }

                for (docConsulta in consultasSnapshot) {
                    val consulta = docConsulta.toObject(Consulta::class.java)
                    val alunoId = consulta.alunoId

                    usuariosRef.document(alunoId).get()
                        .addOnSuccessListener { alunoDoc ->
                            val nomeAluno = alunoDoc.getString("nome") ?: "Aluno sem nome"
                            consultasList.add(
                                ConsultaComNomeAluno(
                                    consulta.data,
                                    consulta.horario,
                                    alunoId,
                                    nomeAluno,
                                    consulta.professorId
                                )
                            )
                            consultasProcessadas++
                            if (consultasProcessadas == totalConsultas) {
                                cb(consultasList)
                            }
                        }
                        .addOnFailureListener {
                            consultasProcessadas++
                            if (consultasProcessadas == totalConsultas) {
                                cb(consultasList)
                            }
                        }
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                cb(emptyList())
            }
    }
}

// Modelo de dados para consulta com nome do aluno já incluído
data class ConsultaComNomeAluno(
    val data: String = "",
    val horario: String = "",
    val alunoId: String = "",
    val alunoNome: String = "",
    val professorId: String = ""
)
