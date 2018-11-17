package br.feevale.projetofinal.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.feevale.projetofinal.R
import kotlinx.android.synthetic.main.activity_edit_trip.*

class EditTripActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_trip)

        tripNameView.text = intent.getStringExtra("tripId")
        //populateTripDetail()
    }

    //private fun populateTripDetail() {
    //}
}
