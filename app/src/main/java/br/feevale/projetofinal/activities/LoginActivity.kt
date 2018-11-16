package br.feevale.projetofinal.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.feevale.projetofinal.R
import br.feevale.projetofinal.services.SharedPreferencesService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val fbAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        createAnAccountButton.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
        loginButton.setOnClickListener { signIn(emailEditTextLogin.text.toString(), passwordEditTextLogin.text.toString()) }
    }

    private fun signIn(email:String, password:String) {
        fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if(task.isSuccessful){
                SharedPreferencesService.write("AuthenticatedUser", "ON")
                startActivity(Intent(this, MainActivity::class.java))
            }else{
                Toast.makeText(this, "Login failed!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
