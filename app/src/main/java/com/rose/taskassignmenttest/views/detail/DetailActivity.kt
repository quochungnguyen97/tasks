package com.rose.taskassignmenttest.views.detail

import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.viewmodels.DetailViewModel
import com.rose.taskassignmenttest.constants.ExtraConstants
import com.rose.taskassignmenttest.views.common.BaseActivity
import javax.inject.Inject

class DetailActivity : BaseActivity(R.string.task) {
    @Inject
    lateinit var mViewModel: DetailViewModel

    companion object {
        private const val TAG = "DetailActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mInjector.inject(this)

        super.onCreate(savedInstanceState)

        intent?.let {
            if (it.getBooleanExtra(ExtraConstants.EXTRA_IS_FROM_NOTI, false)) {
                NotificationManagerCompat.from(this).cancel(ExtraConstants.NOTI_ID)
            }
            val id = it.getIntExtra(ExtraConstants.EXTRA_TASK_ID, -1)
            it.removeExtra(ExtraConstants.EXTRA_TASK_ID)
            Log.i(TAG, "onCreate: id = $id")
            if (id != -1) {
                mViewModel.setTaskId(id)
            }
        }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.base_main_frame, DetailFragment.newInstance())
        }.commit()
    }
}