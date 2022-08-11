package com.example.countries.sharedpreference

import android.content.Context
import com.enctech.sharedprefs.EncTechSharedPrefsHandler
import com.example.countries.Statics

class SharedPreferenceHelper(val context: Context) {

    private val countriesSharedPrefsManager by lazy { EncTechSharedPrefsHandler(context, Statics.MAIN_PREF_NAME) }

    fun isRemoteApiDataFetched() : Boolean = countriesSharedPrefsManager.getBooleanValue(Statics.RemoteApiDataFetched, false)
    fun setRemoteApiDataFetched (isFetched: Boolean=true) = countriesSharedPrefsManager.applyPrefs(Statics.RemoteApiDataFetched, isFetched)

}