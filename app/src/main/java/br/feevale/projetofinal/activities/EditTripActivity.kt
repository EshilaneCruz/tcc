package br.feevale.projetofinal.activities

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.feevale.projetofinal.R
import br.feevale.projetofinal.models.Trip
import br.feevale.projetofinal.services.FirebaseDatabaseService
import br.feevale.projetofinal.utils.UtilMethods
import kotlinx.android.synthetic.main.activity_edit_trip.*

class EditTripActivity : AppCompatActivity() {

    companion object {
        lateinit var trip: Trip
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_trip)

        val tripId = intent.getStringExtra("tripId")
        loadTrip(tripId)

    }

    private fun loadTrip(id: String) {
        FirebaseDatabaseService.firestoreDB.collection("trip").document(id).get().addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document!!.exists()) {
                    trip = Trip(
                            tripOwner = document.get("owner").toString(),
                            tripName = document.get("name").toString(),
                            tripStartDate = document.get("startdate").toString(),
                            tripEndDate = document.get("enddate").toString(),
                            tripBudget = document.get("budget").toString()
                    )
                    setBasicDataOnScreen()
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            } else {
                Log.d(ContentValues.TAG, "get failed with ", task.exception)
            }
        }
    }

    private fun setBasicDataOnScreen() {
        tripNameView.text = trip.tripName
        val formatedDate = "${UtilMethods.formatDate(trip.tripStartDate)} - ${UtilMethods.formatDate(trip.tripEndDate)}"
        tripPeriodView.text = formatedDate
        val formatedValue = "$ ${trip.tripBudget}.00"
        budgetView.text = formatedValue
    }

}
