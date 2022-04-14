package com.example.smartalarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartalarm.adapter.AlarmAdapter
import com.example.smartalarm.data.Alarm
import com.example.smartalarm.databinding.ActivityMainBinding
import com.example.smartalarm.local.AlarmDB
import com.example.smartalarm.local.AlarmDao
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var _bindin: ActivityMainBinding? = null
    private val binding get() = _bindin as ActivityMainBinding

    // utk manggil alarmdao
    private var alarmDao: AlarmDao? = null

    private var alarmAdapter: AlarmAdapter? = null

    private var alarmService: AlarmService? = null

    override fun onResume() {
        super.onResume()
        alarmDao?.getAlarm()?.observe(this){
            alarmAdapter?.setData(it)
            Log.i("GetAlarm", "getAlarm: alarm with $it")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bindin = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        alarmAdapter = AlarmAdapter()
        alarmService = AlarmService()

        // utk akses database
        val db = AlarmDB.getDatabase(applicationContext)
        alarmDao = db.alarmDao()


        initView()
        setupRecyclerView()

    }

    // set Up recyclerView
    private fun setupRecyclerView() {
        binding.rvReminderAlarm.apply {
            layoutManager = LinearLayoutManager(context)
            adapter= alarmAdapter
            swipeToDelete(this)
        }
    }

    // function init view
    private fun initView() {
        binding.apply {
            viewSetOneTimeAlarm.setOnClickListener {
                startActivity(Intent(applicationContext, OneTimeAlarmActivity::class.java))
            }

            viewSetRepeatingAlarm.setOnClickListener {
                startActivity(Intent(this@MainActivity, RepeatingAlarmActivity::class.java))
                finish()
            }
        }
//        tv_time_today.format24Hour
    }

    // Delete function
    private fun swipeToDelete(recyclerView: RecyclerView) {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedAlarm = alarmAdapter?.listAlarm?.get(viewHolder.adapterPosition)
                CoroutineScope(Dispatchers.IO).launch{
                    deletedAlarm?.let { alarmDao?.deleteAlarm(it) }
                    Log.i("DeleteAlarm", "onSwiped: deleteAlarm $deletedAlarm")
                    }
                    val alarmType = deletedAlarm?.type
                    alarmType?.let { alarmService?.cancelAlarm(baseContext, it) }
            }

        }).attachToRecyclerView(recyclerView)
    }
}