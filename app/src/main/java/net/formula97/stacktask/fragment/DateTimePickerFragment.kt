package net.formula97.stacktask.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import net.formula97.stacktask.R
import java.util.*

class DateTimePickerFragment: DialogFragment() {

    companion object {
        const val FRAGMENT_TAG = "net.formula97.stacktask.fragment.DateTimePickerFragment.FRAGMENT_TAG"

        private var calendarBundleKey = "calendarBundleKey"

        fun create(calendar: Calendar): DateTimePickerFragment {
            val args = Bundle()
            args.putSerializable(calendarBundleKey, calendar)

            val frag = DateTimePickerFragment()
            frag.arguments = args

            return frag
        }
    }

    interface OnDateTimeSetListener: EventListener {
        fun onDateTimeSet(calendar: Calendar)
    }

    private lateinit var callback: OnDateTimeSetListener

    fun setOnDateTimeSetListener(callback: OnDateTimeSetListener) {
        this.callback = callback
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val currentCalendar = arguments!!.getSerializable(calendarBundleKey) as Calendar
        val year = currentCalendar.get(Calendar.YEAR)
        val month = currentCalendar.get(Calendar.MONTH)
        val dayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH)
        val hour = currentCalendar.get(Calendar.HOUR_OF_DAY)
        val minute = currentCalendar.get(Calendar.MINUTE)

        val builder = AlertDialog.Builder(activity!!)
        val bodyView = View.inflate(activity!!, R.layout.date_time_picker_dialog, null)

        val dateView = bodyView.findViewById<DatePicker>(R.id.date_view)
        val timeView = bodyView.findViewById<TimePicker>(R.id.time_view)

        val retCalendar = Calendar.getInstance()
        retCalendar.timeInMillis = currentCalendar.timeInMillis

        dateView.init(year, month, dayOfMonth) { _, yyyy, mm, dd ->
            // TODO 年月日取得後の処理を書く
            retCalendar.set(Calendar.YEAR, yyyy)
            retCalendar.set(Calendar.MONTH, mm)
            retCalendar.set(Calendar.DAY_OF_MONTH, dd)
            timeView.visibility = View.VISIBLE
            dateView.visibility = View.GONE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timeView.hour = hour
            timeView.minute = minute
        } else {
            timeView.currentHour = hour
            timeView.currentMinute = minute
        }

        timeView.setOnTimeChangedListener { _, hh, mm ->
            // TODO 時分取得後の処理を書く
            retCalendar.set(Calendar.HOUR_OF_DAY, hh)
            retCalendar.set(Calendar.MINUTE, mm)
            dateView.visibility = View.VISIBLE
            timeView.visibility = View.GONE
        }

        builder.setView(bodyView)
                .setPositiveButton(R.string.determine) { dialog, which ->
                    callback.onDateTimeSet(retCalendar)
                }

        return builder.create()
    }
}