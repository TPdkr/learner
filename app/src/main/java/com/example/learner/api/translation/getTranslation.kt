package com.example.learner.api.translation

import android.util.Log
import com.example.learner.api.RetrofitInstance

suspend fun getTranslation(word: String, sourceLang: String = "de", targetLang: String = "en"): String {
    try {
        Log.d("LingvaTranslate", "call started")
        val response = RetrofitInstance.lingvaApiService.translation(sourceLang, targetLang, word).translation
        Log.d("LingvaTranslate", response)
        return response
    } catch(e: Exception){
        Log.e("LingvaTranslate", e.message?:"no message given")
        return ""
    }
}