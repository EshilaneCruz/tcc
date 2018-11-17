package br.feevale.projetofinal.activities

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.feevale.projetofinal.R
import br.feevale.projetofinal.models.Trip
import br.feevale.projetofinal.services.FirebaseDatabaseService
import kotlinx.android.synthetic.main.activity_edit_trip.*

class EditTripActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_trip)

        val tripId = intent.getStringExtra("tripId")

        FirebaseDatabaseService.firestoreDB.collection("trip").document(tripId).get().addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    tripNameView.text = document.get("name").toString()
                    val endDate = document.get("enddate").toString()
                    val startDate = document.get("startdate").toString()
                    val formatedDate = "$startDate to $endDate"
                    tripPeriodView.text = formatedDate
                    budgetView.text = document.get("budget").toString()
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            } else {
                Log.d(ContentValues.TAG, "get failed with ", task.exception)
            }
        }
    }

}
