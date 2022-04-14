package com.example.smartalarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.smartalarm.data.Alarm
import com.example.smartalarm.databinding.ActivityRepeatingAlarmBinding
import com.example.smartalarm.fragment.DatePickerFragment
import com.example.smartalarm.fragment.TimePickerFragment
import com.example.smartalarm.helper.TAG_TIME_PICKER
import com.example.smartalarm.local.AlarmDB
import com.example.smartalarm.local.AlarmDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RepeatingAlarmActivity : AppCompatActivity(), TimePickerFragment.TimeDialogListener {

    private var _binding: ActivityRepeatingAlarmBinding? = null
    private val binding get() = _binding as ActivityRepeatingAlarmBinding

    private var alarmDao: AlarmDao? = null

    private var _alarmService: AlarmService? = null
    private val alarmService get() = _alarmService as AlarmService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityRepeatingAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AlarmDB.getDatabase(this)
        alarmDao = db.alarmDao()

        _alarmService = AlarmService()

        initView()
    }

    private fun initView() {
        binding.apply{

            btnSetTimeRepeating.setOnClickListener{
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(supportFragmentManager, TAG_TIME_PICKER)
            }

            btnAddSetRepeatingAlarm.setOnClickListener{
                val time = tvRepeatingTime.text.toString()
                val message = etNoteRepeating.text.toString()

                if (time != "Time"){
                    alarmService.setRepeatingAlarm(
                        applicationContext,
                        AlarmService.TYPE_REPEATING,
                        time,
                        message
                    )
                    CoroutineScope(Dispatchers.IO).launch{
                        alarmDao?.addAlarm(Alarm(
                            0,
                            "Repeating Alarm",
                            time,
                            message,
                            AlarmService.TYPE_REPEATING
                        ))
                        finish()
                    }
                } else{
                    Toast.makeText(
                        this@RepeatingAlarmActivity,
                        "Please set time of alarm",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(hourOfDay, minute)
        val dateFormat = SimpleDateFormat("hh:mm", Locale.getDefault())

        binding.tvRepeatingTime.text = dateFormat.format(calendar.time)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}