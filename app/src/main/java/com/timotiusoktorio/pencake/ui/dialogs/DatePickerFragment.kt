package com.timotiusoktorio.pencake.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import java.util.*

class DatePickerFragment : DialogFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is DatePickerDialog.OnDateSetListener) {
            throw IllegalStateException("${context.javaClass.simpleName} must implements DatePickerDialog.OnDateSetListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(activity, activity as DatePickerDialog.OnDateSetListener, year, month, day)
    }
}