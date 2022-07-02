package com.rose.taskassignmenttest.views.detail

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TimePicker
import java.util.*

class DateTimeHandler(private val mContext: Context, private val mOnDateTimeSet: (Long) -> Unit) :
    TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {

    private val mCalendar: Calendar = Calendar.getInstance()

    fun showDialogs() {
        val currentCalendar = Calendar.getInstance()
        currentCalendar.timeInMillis = System.currentTimeMillis()
        DatePickerDialog(
            mContext, this, currentCalendar.get(Calendar.YEAR),
            currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        mCalendar.set(Calendar.MINUTE, minute)

        mOnDateTimeSet(mCalendar.timeInMillis)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mCalendar.set(Calendar.YEAR, year)
        mCalendar.set(Calendar.MONTH, month)
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val currentCalendar = Calendar.getInstance()
        currentCalendar.timeInMillis = System.currentTimeMillis()

        TimePickerDialog(
            mContext, this, currentCalendar.get(Calendar.HOUR_OF_DAY),
            currentCalendar.get(Calendar.MINUTE), true
        ).show()
    }
}