package br.feevale.projetofinal.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import br.feevale.projetofinal.R
import br.feevale.projetofinal.services.SharedPreferencesService

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val sharedPreferences =
                this.getSharedPreferences("BeegoingApp", Context.MODE_PRIVATE)
        SharedPreferencesService.sharedPreferences = sharedPreferences
        Handler().postDelayed({
            openApp()
        }, 2000)
    }

    private fun openApp() {
        val intent : Intent
        if("ON" == SharedPreferencesService.retrieveString("AuthenticatedUser")) {
            intent = Intent(this, MainActivity::class.java)
        }else{
            intent = Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
    }
}
