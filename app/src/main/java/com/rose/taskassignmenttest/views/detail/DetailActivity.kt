package com.rose.taskassignmenttest.views.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.viewmodels.DetailViewModel
import com.rose.taskassignmenttest.constants.ExtraConstants
import com.rose.taskassignmenttest.viewmodels.idaos.TaskDaoFactory

class DetailActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "DetailActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        viewModel.setTaskDao(TaskDaoFactory.newTaskDao(this))

        intent?.let {
            val id = it.getIntExtra(ExtraConstants.EXTRA_TASK_ID, -1)
            Log.i(TAG, "onCreate: id = $id")
            viewModel.setTaskId(id)
        }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.detail_main_frame, DetailFragment.newInstance())
        }.commit()
    }
}