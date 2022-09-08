package com.rose.taskassignmenttest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rose.taskassignmenttest.constants.ExtraConstants
import com.rose.taskassignmenttest.utils.NotiChannelManager
import com.rose.taskassignmenttest.viewmodels.ListViewModel
import com.rose.taskassignmenttest.viewmodels.idaos.TaskDaoFactory
import com.rose.taskassignmenttest.views.detail.DetailActivity
import com.rose.taskassignmenttest.views.list.TaskListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val viewModel = ViewModelProvider(this)[ListViewModel::class.java]
        viewModel.setTaskDao(TaskDaoFactory.newTaskDao(this))

        val activityResultStarter =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val reloadList = result.data?.getBooleanExtra(
                        ExtraConstants.EXTRA_RELOAD_LIST,
                        false
                    ) ?: false
                    if (reloadList) {
                        viewModel.loadAllTasks()
                    }
                }
            }

        findViewById<FloatingActionButton>(R.id.main_add_button)
            .setOnClickListener {
                activityResultStarter.launch(
                    Intent(
                        applicationContext,
                        DetailActivity::class.java
                    ).apply {
                        putExtra(ExtraConstants.EXTRA_TASK_ID, -1)
                    })
            }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_frame, TaskListFragment.newInstance())
        }.commit()

        NotiChannelManager.createChannel(this)
    }
}