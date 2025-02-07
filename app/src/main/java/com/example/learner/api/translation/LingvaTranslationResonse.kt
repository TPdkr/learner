package com.example.learner.api.translation

import kotlinx.serialization.Serializable

@Serializable
data class LingvaTranslationResponse(
    val translation: String
)