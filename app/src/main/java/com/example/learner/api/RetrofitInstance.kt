package com.example.learner.api

import com.example.learner.api.translation.LingvaApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitInstance {
    private const val BASE_LINGVA_URL = "https://lingva.ml/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_LINGVA_URL)
        .build()

    val lingvaApiService: LingvaApiService
        by lazy { retrofit.create(LingvaApiService::class.java) }


}