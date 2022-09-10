package com.rose.taskassignmenttest.views.detail

import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.viewmodels.DetailViewModel
import com.rose.taskassignmenttest.constants.ExtraConstants
import com.rose.taskassignmenttest.views.common.BaseActivity

class DetailActivity : BaseActivity(R.string.task) {
    companion object {
        private const val TAG = "DetailActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        viewModel.setTaskDao(provideTaskDao())

        intent?.let {
            if (it.getBooleanExtra(ExtraConstants.EXTRA_IS_FROM_NOTI, false)) {
                NotificationManagerCompat.from(this).cancel(ExtraConstants.NOTI_ID)
            }
            val id = it.getIntExtra(ExtraConstants.EXTRA_TASK_ID, -1)
            Log.i(TAG, "onCreate: id = $id")
            viewModel.setTaskId(id)
        }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.base_main_frame, DetailFragment.newInstance())
        }.commit()
    }
}