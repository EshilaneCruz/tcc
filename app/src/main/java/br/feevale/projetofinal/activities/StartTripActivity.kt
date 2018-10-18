package br.feevale.projetofinal.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.feevale.projetofinal.R
import br.feevale.projetofinal.dialogs.DatePickerFragment
import kotlinx.android.synthetic.main.activity_start_trip.*

class StartTripActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_trip)

        pickStartDateButton.setOnClickListener { showStartDatePickerDialog() }
    }

    fun showStartDatePickerDialog() {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }
}
