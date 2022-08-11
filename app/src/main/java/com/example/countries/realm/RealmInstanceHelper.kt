package com.example.countries.realm

import io.realm.*

object RealmInstanceHelper {
    private val realm: Realm by lazy {
        Realm.setDefaultConfiguration(configuration)
        Realm.getDefaultInstance() }

    private val configuration by lazy { RealmConfiguration.Builder()
        .allowWritesOnUiThread(true)
        .allowQueriesOnUiThread(true)
        .build()
    }

    fun getInstance() : Realm = realm
}