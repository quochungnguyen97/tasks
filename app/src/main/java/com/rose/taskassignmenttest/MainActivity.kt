package com.rose.taskassignmenttest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rose.taskassignmenttest.utils.NotiChannelManager
import com.rose.taskassignmenttest.views.list.TaskListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_frame, TaskListFragment.newInstance())
        }.commit()

        NotiChannelManager.createChannel(this)
    }
}