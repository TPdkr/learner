package com.example.learner.data

import android.app.Application

class LearnerApp : Application() {
    //the container of the app data
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}