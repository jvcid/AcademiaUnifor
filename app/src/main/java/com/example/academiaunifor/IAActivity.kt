package com.example.academiaunifor

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IAActivity : AppCompatActivity() {

    private lateinit var textMensagem: EditText
    private lateinit var btnEnviar: FloatingActionButton
    private lateinit var textResposta: TextView

    // ⚠️ IMPORTANTE: Não exponha sua API key assim em produção
    private val apiKey = "AIzaSyCSmL99RG_y6H9ZOeDvohI9_fqkGFQ3d0c"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ia)

        textMensagem = findViewById(R.id.editTextTextMensagem)
        btnEnviar = findViewById(R.id.floatingActionButtonEnviar)
        textResposta = findViewById(R.id.textResposta)

        btnEnviar.setOnClickListener {
            val textoUsuario = textMensagem.text.toString().trim()
            if (textoUsuario.isNotBlank()) {
                appendChat("Você: $textoUsuario")
                textMensagem.text.clear()
                sendGeminiRequest(textoUsuario)
            }
        }
    }

    private fun appendChat(text: String) {
        textResposta.append("$text\n\n")
    }

    private fun sendGeminiRequest(userText: String) {
        val request = GeminiRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = userText)
                    )
                )
            )
        )

        RetrofitClient.instance.generateContent(apiKey, request)
            .enqueue(object : Callback<GeminiResponse> {
                override fun onResponse(
                    call: Call<GeminiResponse>,
                    response: Response<GeminiResponse>
                ) {
                    if (response.isSuccessful) {
                        val reply = response.body()?.candidates
                            ?.firstOrNull()
                            ?.content
                            ?.parts
                            ?.firstOrNull()
                            ?.text ?: "Sem resposta da IA"

                        Log.d("Gemini", "Resposta da IA: $reply")
                        appendChat("IA: $reply")
                    } else {
                        val erro = response.errorBody()?.string() ?: "Erro desconhecido"
                        Log.e("Gemini", "Erro HTTP: $erro")
                        appendChat("Erro na resposta da IA.")
                    }
                }

                override fun onFailure(call: Call<GeminiResponse>, t: Throwable) {
                    Log.e("Gemini", "Falha na requisição: ${t.message}", t)
                    appendChat("Erro de rede: ${t.localizedMessage}")
                }
            })
    }
}
