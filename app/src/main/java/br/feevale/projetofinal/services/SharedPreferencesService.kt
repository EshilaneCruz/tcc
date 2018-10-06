package br.feevale.projetofinal.services

import android.content.SharedPreferences

object SharedPreferencesService {
    lateinit var sharedPreferences: SharedPreferences

    fun write(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun retrieveString(key: String) = sharedPreferences.getString(key, "")
}