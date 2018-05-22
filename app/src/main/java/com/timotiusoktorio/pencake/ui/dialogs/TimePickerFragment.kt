package com.timotiusoktorio.pencake.ui.dialogs

import android.app.Dialog
import android.app.DialogFragment
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import java.util.*

class TimePickerFragment : DialogFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is TimePickerDialog.OnTimeSetListener) {
            throw IllegalStateException("${context.javaClass.simpleName} must implements TimePickerDialog.OnTimeSetListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(activity, activity as TimePickerDialog.OnTimeSetListener,
                hour, minute, DateFormat.is24HourFormat(activity))
    }
}