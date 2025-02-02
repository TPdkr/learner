package com.example.learner.api.translation

import retrofit2.http.GET
import retrofit2.http.Path

interface LingvaApiService {
    @GET("api/v1/{sourceLang}/{targetLang}/{text}")
    suspend fun translation(
        @Path("sourceLang") sourceLang: String,
        @Path("targetLang") targetLang: String,
        @Path("text") text: String
    ): LingvaTranslationResponse
}