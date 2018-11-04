package br.feevale.projetofinal.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.feevale.projetofinal.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_start_trip.*
import java.text.SimpleDateFormat
import java.util.*



class StartTripActivity : AppCompatActivity() {
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestoreDB = FirebaseFirestore.getInstance()
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    lateinit var startDate : String
    lateinit var endDate : String
    var startDateInMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_trip)

        start_date_calendar.setOnDateChangeListener{_, year, month, day ->
            val c = Calendar.getInstance()
            c.set(year, month, day)
            startDateInMillis = c.timeInMillis
            startDate = dateFormat.format(c.timeInMillis)

        }

        end_date_calendar.setOnDateChangeListener{_, year, month, day ->
            val c = Calendar.getInstance()
            c.set(year, month, day)
            if(c.timeInMillis <= startDateInMillis){
                Toast.makeText(this, "End date is before start date, please choose another date.", Toast.LENGTH_LONG).show()
            }else{
                endDate = dateFormat.format(c.timeInMillis)
            }
        }

        addTripButton.setOnClickListener { saveNewTrip() }
    }

    private fun saveNewTrip() {
        val tripInformation = mapOf(
                "name" to tripNameAddTrip.text.toString(),
                "startdate" to startDate,
                "enddate" to endDate,
                "budget" to tripBudgetAddTrip.text.toString(),
                "owner" to firebaseAuth.currentUser?.email.toString())
        firestoreDB.collection("trip").document().set(tripInformation).addOnSuccessListener { startActivity(Intent(this, MainActivity::class.java)) }

    }

}
