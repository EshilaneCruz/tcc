package br.feevale.projetofinal.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import android.widget.TimePicker
import br.feevale.projetofinal.services.SharedPreferencesService
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        var timePicked = ""
        if(minute == 0){
            timePicked = "$hourOfDay:00"
        }
        else{
            timePicked = "$hourOfDay:$minute"
        }
        SharedPreferencesService.write("TimePickerData",timePicked)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        val activity = activity
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }
}