package com.example.countries

import android.app.Application
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.realm.Realm

class Countries : Application() {

    override fun onCreate() {
        super.onCreate()

        // initialize Realm
        Realm.init(this)
    }
}