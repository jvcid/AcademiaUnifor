package com.example.academiaunifor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.Call

interface GeminiApi {
    @Headers("Content-Type: application/json")
    @POST("models/gemini-2.0-flash:generateContent")
    fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): Call<GeminiResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/"

    val instance: GeminiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApi::class.java)
    }
}

