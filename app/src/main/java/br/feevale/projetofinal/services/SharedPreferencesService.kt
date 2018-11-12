package br.feevale.projetofinal.services

import android.content.SharedPreferences

object SharedPreferencesService {
    lateinit var sharedPreferences: SharedPreferences

    fun write(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun write(key: String, value: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun retrieveString(key: String) = sharedPreferences.getString(key, "")
    fun retrieveLong(key: String) = sharedPreferences.getLong(key, 0)
}