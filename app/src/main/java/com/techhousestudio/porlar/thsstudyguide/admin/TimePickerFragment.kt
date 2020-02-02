package com.techhousestudio.porlar.thsstudyguide.admin


import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    lateinit var setOnTimeClickListener: SetOnTimeClickListener


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val c: Calendar = Calendar.getInstance()
        val hour: Int = c.get(Calendar.HOUR_OF_DAY)
        val minute: Int = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(
            activity,  this, hour, minute,
            DateFormat.is24HourFormat(activity)
        )
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val isStart = arguments?.getBoolean("is_start",false)
        setOnTimeClickListener.onTimeClick("$hourOfDay:$minute",isStart)
    }


}
