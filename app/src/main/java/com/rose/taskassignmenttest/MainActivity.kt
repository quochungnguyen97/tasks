package com.rose.taskassignmenttest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rose.taskassignmenttest.views.list.TaskListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_frame, TaskListFragment.newInstance())
        }.commit()
    }
}