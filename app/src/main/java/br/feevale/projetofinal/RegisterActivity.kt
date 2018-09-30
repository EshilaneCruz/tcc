package br.feevale.projetofinal

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import android.util.Log


class RegisterActivity : AppCompatActivity() {
    private val RESULT_LOAD_IMAGE = 1
    val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerButton.setOnClickListener { registerNewAccount(emailEditTextRegister.text.toString(),
                passwordEditTextRegister.text.toString(),
                givenNameEditTextRegister.text.toString(),
                familyNameEditTextRegister.text.toString(),
                cpfEditTextRegister.text.toString()) }
    }

    private val authClient: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val databaseClient: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private fun registerNewAccount(email: String,
                                   password: String,
                                   givenName: String,
                                   familyName: String,
                                   cpf: String) {
        if (!email.isBlank()
            && !password.isBlank()
            && !givenName.isBlank()
            && !familyName.isBlank()
            && !cpf.isBlank()) {
            authClient.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            createAccountProfile(email, givenName, familyName, cpf)
                        } else {
                            Toast.makeText(this, R.string.registration_error, Toast.LENGTH_LONG).show()
                        }
                    }
        }else{
            Toast.makeText(this, R.string.toast_error_required, Toast.LENGTH_LONG).show()
            if(email.isBlank()){
                emailEditTextRegister?.hint = "Email is required!"
            }
            if(password.isBlank()){
                passwordEditTextRegister?.hint = "Password is required!"
            }
            if(givenName.isBlank()){
                givenNameEditTextRegister?.hint = "Given name is required!"
            }
            if(familyName.isBlank()){
                familyNameEditTextRegister?.hint = "Family name is required!"
            }
            if(cpf.isBlank()){
                cpfEditTextRegister?.hint = "CPF is required!"
            }
        }
    }

    private fun createAccountProfile(email: String,
                                     givenName: String,
                                     familyName: String,
                                     cpf: String) {
        val profileInformation = mapOf(
                "birthdate" to birthDateEditTextRegister.text.toString(),
                "cellphone" to cellphoneEditTextRegister.text.toString(),
                "city" to cityOfResidenceEditTextRegister.text.toString(),
                "cpf" to cpf,
                "email" to email,
                "familyname" to familyName,
                "givenname" to givenName)

        databaseClient.collection("users").document()
                .set(profileInformation)
                .addOnSuccessListener{startActivity(Intent(this, MainActivity::class.java))}
                .addOnFailureListener{Toast.makeText(this, R.string.registration_error, Toast.LENGTH_LONG).show()}

    }

}
