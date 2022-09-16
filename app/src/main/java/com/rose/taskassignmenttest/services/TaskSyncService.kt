package com.rose.taskassignmenttest.services

import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.constants.ActionConstants
import com.rose.taskassignmenttest.constants.ExtraConstants
import com.rose.taskassignmenttest.constants.PreferenceConstants
import com.rose.taskassignmenttest.services.models.SyncLocalDatabaseModel
import com.rose.taskassignmenttest.services.models.SyncServerModel
import com.rose.taskassignmenttest.utils.PreferenceUtils
import kotlinx.coroutines.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

class TaskSyncService : BaseService() {

    @Inject
    lateinit var mSyncServerModel: SyncServerModel
    @Inject
    lateinit var mSyncLocalDatabaseModel: SyncLocalDatabaseModel

    private val mCoroutineScope = CoroutineScope(Dispatchers.Main)
    private val mExceptionHandler = CoroutineExceptionHandler { _, e ->
        onExceptionThrown(e)
    }

    override fun onCreate() {
        super.onCreate()
        mInjector.inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mCoroutineScope.launch(mExceptionHandler) {
            PreferenceUtils.setPreference(
                applicationContext,
                PreferenceConstants.PREF_KEY_IS_SYNCING,
                true
            )

            val allTasks = mSyncLocalDatabaseModel.getAllTasksFromDb()
            val responseTasksFromServer = mSyncServerModel.requestSyncTasks(
                allTasks, PreferenceUtils.getAccountToken(this@TaskSyncService)
            )

            val isResponseTasksSaved = if (responseTasksFromServer.isNotEmpty()) {
                mSyncLocalDatabaseModel.saveTasksToDb(responseTasksFromServer)
            } else {
                true
            }

            PreferenceUtils.setPreference(
                applicationContext,
                PreferenceConstants.PREF_KEY_IS_SYNCING,
                false
            )

            val message = if (isResponseTasksSaved) {
                R.string.sync_success
            } else {
                R.string.sync_failed
            }
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

            val resultIntent = Intent(ActionConstants.SYNC_DONE_ACTION)
            resultIntent.putExtra(ExtraConstants.SYNC_RESULT, isResponseTasksSaved)
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(resultIntent)
        }
        return START_STICKY
    }

    private fun onExceptionThrown(e: Throwable) {
        Log.e(TAG, "onExceptionThrown: ", e)
        when (e) {
            is SocketTimeoutException -> {
                Toast.makeText(applicationContext, R.string.server_timeout, Toast.LENGTH_SHORT)
                    .show()
            }
            is ConnectException -> {
                Toast.makeText(applicationContext, R.string.connection_failed, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")

        PreferenceUtils.setPreference(
            applicationContext,
            PreferenceConstants.PREF_KEY_IS_SYNCING,
            false
        )
        mCoroutineScope.coroutineContext.cancelChildren()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val TAG = "TaskSyncService"
    }
}