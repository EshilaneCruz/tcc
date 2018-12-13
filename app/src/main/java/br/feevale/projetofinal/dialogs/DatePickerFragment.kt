package br.feevale.projetofinal.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import br.feevale.projetofinal.services.SharedPreferencesService
import java.util.*
import android.content.DialogInterface


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    lateinit var c: Calendar

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        var monthSet = month+1
        val date = "$day-$monthSet-$year"
        SharedPreferencesService.write("DatePickerData",date)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        val activity = activity
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }

}
