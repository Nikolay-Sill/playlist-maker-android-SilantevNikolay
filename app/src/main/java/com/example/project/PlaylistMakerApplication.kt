package com.example.project

import android.app.Application
import com.example.project.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PlaylistMakerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PlaylistMakerApplication)
            modules(appModule)
        }
    }
}