package br.feevale.projetofinal

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            goToLogin()
        }, 2000)
    }

    private fun goToLogin() {
        val intent : Intent = Intent(this,  LoginActivity :: class.java)
        startActivity(intent)    }
}
