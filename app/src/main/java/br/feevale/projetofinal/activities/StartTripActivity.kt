package br.feevale.projetofinal.activities

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.feevale.projetofinal.R
import br.feevale.projetofinal.dialogs.DatePickerFragment
import br.feevale.projetofinal.services.FirebaseDatabaseService
import br.feevale.projetofinal.services.SharedPreferencesService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_start_trip.*
import java.text.SimpleDateFormat


class StartTripActivity : AppCompatActivity(), DialogInterface.OnDismissListener {

    val firebaseAuth = FirebaseAuth.getInstance()
    var startDate : String = ""
    var endDate : String = ""
    var startDateInMillis: Long = 0
    var endDateInMillis: Long = 0
    val dateformat = SimpleDateFormat("dd-MM-yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_trip)

        start_date_button.setOnClickListener{
            showStartDatePickerDialog()
        }

        end_date_button.setOnClickListener{
            showEndDatePickerDialog()
        }

        addTripButton.setOnClickListener { saveNewTrip() }
    }

    private fun saveNewTrip() {
        val tripName = tripNameAddTrip.text.toString()
        val budget = tripBudgetAddTrip.text.toString()
        if (tripName.isNotEmpty() && budget.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty()) {
            val tripInformation = mapOf(
                    "name" to tripName,
                    "startdate" to startDate,
                    "enddate" to endDate,
                    "budget" to budget,
                    "owner" to firebaseAuth.currentUser?.email.toString())
            val tripId = FirebaseDatabaseService.firestoreDB.collection("trip").document()
            tripId.set(tripInformation).addOnCompleteListener{ startActivity(Intent(this, EditTripActivity::class.java).putExtra("tripId", tripId.id))}
        }
    }

    private fun showStartDatePickerDialog() {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }

    private fun showEndDatePickerDialog() {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDismiss(dialog: DialogInterface?) {
        if(startDate.isEmpty()){
            setStartDate()
        }else{
            setEndDate()
        }
    }

    private fun setStartDate() {
        startDate = SharedPreferencesService.retrieveString("DatePickerData")
        val date = dateformat.parse(startDate)
        startDateInMillis = date.time
        start_date_view.setText(startDate)
    }

    private fun setEndDate() {
        endDate = SharedPreferencesService.retrieveString("DatePickerData")
        val date = dateformat.parse(endDate)
        endDateInMillis = date.time
        end_date_view.setText(endDate)
    }

}
