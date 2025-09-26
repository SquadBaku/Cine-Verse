package com.karrar.movieapp

import android.app.Application
import android.content.Context
import com.karrar.movieapp.utilities.LanguageManager
import com.karrar.movieapp.utilities.PrefsManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovieApplication :Application(){
    override fun attachBaseContext(base: Context) {
        val lang = PrefsManager.getLanguage(base)
        val context = LanguageManager.setLocale(base, lang)
        super.attachBaseContext(context)
    }
}