package com.rose.taskassignmenttest.views.list

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.constants.ActionConstants
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.viewmodels.ListViewModel
import com.rose.taskassignmenttest.constants.ExtraConstants
import com.rose.taskassignmenttest.constants.PreferenceConstants
import com.rose.taskassignmenttest.services.TaskSyncService
import com.rose.taskassignmenttest.utils.PreferenceUtils
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.views.account.AccountActivity
import com.rose.taskassignmenttest.views.detail.DetailActivity
import com.rose.taskassignmenttest.views.list.items.ItemsSorter
import com.rose.taskassignmenttest.views.login.LoginActivity
import kotlinx.coroutines.*


class TaskListFragment : Fragment(), TaskListListener {
    private lateinit var mListAdapter: ListAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mEmptyText: TextView

    private lateinit var mListViewModel: ListViewModel

    private val mCoroutineScope = CoroutineScope(Dispatchers.Main)

    private val mStartActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(result)
        }

    private val mTaskListBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(receiverContext: Context?, intent: Intent?) {
            intent?.let {
                val action: String = it.action ?: StringUtils.EMPTY
                if (action == ActionConstants.SYNC_DONE_ACTION) {
                    val isSyncSuccess = it.getBooleanExtra(ExtraConstants.SYNC_RESULT, false)
                    if (isSyncSuccess) {
                        mListViewModel.loadAllTasks()
                    }
                    requireActivity().invalidateOptionsMenu()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = TaskListFragment()
        private const val TAG = "TaskListFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_task_list, container, false)

        initViewElements(root)

        initViewModel()

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            mTaskListBroadcastReceiver,
            IntentFilter(ActionConstants.SYNC_DONE_ACTION)
        )

        setHasOptionsMenu(true)

        return root
    }

    private fun initViewElements(root: View) {
        mRecyclerView = root.findViewById(R.id.list)
        mEmptyText = root.findViewById(R.id.empty_text)

        mListAdapter = ListAdapter(requireContext(), this)
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.adapter = mListAdapter
    }

    private fun initViewModel() {
        activity?.let {
            mListViewModel = ViewModelProvider(it).get(ListViewModel::class.java)
            mListViewModel.loadAllTasks()
            mListViewModel.getAllTasks().observe(it) { tasks -> updateTasks(tasks) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.task_list_option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        context?.let {
            val isHideAccount = PreferenceUtils.getBooleanPreference(
                it,
                PreferenceConstants.PREF_KEY_HIDE_COMPLETED
            )
            if (isHideAccount) {
                menu.findItem(R.id.task_list_menu_item_hide_completed)?.isVisible = false
                menu.findItem(R.id.task_list_menu_item_show_completed)?.isVisible = true
            } else {
                menu.findItem(R.id.task_list_menu_item_hide_completed)?.isVisible = true
                menu.findItem(R.id.task_list_menu_item_show_completed)?.isVisible = false
            }

            val isLoggedIn = !StringUtils.isEmptyOrBlank(PreferenceUtils.getAccountToken(it))
            if (isLoggedIn) {
                menu.findItem(R.id.task_list_menu_login)?.isVisible = false
                menu.findItem(R.id.task_list_menu_register)?.isVisible = false
                menu.findItem(R.id.task_list_menu_account)?.isVisible = true
                menu.findItem(R.id.task_list_menu_sync)?.let { syncItem ->
                    syncItem.isVisible = true
                    syncItem.isEnabled = !PreferenceUtils.getBooleanPreference(
                        requireContext(),
                        PreferenceConstants.PREF_KEY_IS_SYNCING
                    )
                }
            } else {
                menu.findItem(R.id.task_list_menu_login)?.isVisible = true
                menu.findItem(R.id.task_list_menu_register)?.isVisible = true
                menu.findItem(R.id.task_list_menu_account)?.isVisible = false
                menu.findItem(R.id.task_list_menu_sync)?.isVisible = false
            }
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.task_list_menu_item_show_completed -> {
                updateHideCompleted(false)
                true
            }
            R.id.task_list_menu_item_hide_completed -> {
                updateHideCompleted(true)
                true
            }
            R.id.task_list_menu_login -> {
                openLoginScreen()
                true
            }
            R.id.task_list_menu_account -> {
                openAccountScreen()
                true
            }
            R.id.task_list_menu_sync -> {
                startSync()
                Toast.makeText(requireContext(), R.string.started_sync, Toast.LENGTH_SHORT).show()
                item.isEnabled = false
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun updateHideCompleted(hideCompleted: Boolean) {
        activity?.let {
            PreferenceUtils.setPreference(
                it,
                PreferenceConstants.PREF_KEY_HIDE_COMPLETED,
                hideCompleted
            )
            mListViewModel.loadAllTasks()
            it.invalidateOptionsMenu()
        }
    }

    private fun openLoginScreen() {
        activity?.let {
            mStartActivityForResult.launch(Intent(it.applicationContext, LoginActivity::class.java))
        }
    }

    private fun openAccountScreen() {
        activity?.let {
            mStartActivityForResult.launch(
                Intent(
                    it.applicationContext,
                    AccountActivity::class.java
                )
            )
        }
    }

    private fun handleActivityResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                val reloadList = data.getBooleanExtra(
                    ExtraConstants.EXTRA_RELOAD_LIST,
                    false
                )
                val reloadMenu = data.getBooleanExtra(
                    ExtraConstants.EXTRA_RELOAD_MENU,
                    false
                )
                val startSync = data.getBooleanExtra(
                    ExtraConstants.EXTRA_START_SYNC,
                    false
                )

                if (reloadList) {
                    mListViewModel.loadAllTasks()
                }

                if (reloadMenu) {
                    activity?.invalidateOptionsMenu()
                }

                if (startSync) {
                    startSync()
                }
            }
        }
    }

    private fun startSync() {
        activity?.let {
            it.startService(Intent(it, TaskSyncService::class.java))
        }
    }

    private fun updateTasks(tasks: MutableList<Task>) {
        mCoroutineScope.launch {
            withContext(Dispatchers.IO) {
                mListAdapter.updateItems(
                    ItemsSorter.getGroupSortItemsList(
                        tasks,
                        ItemsSorter.GROUP_BY_STATUS,
                        requireContext()
                    )
                )
            }
            mListAdapter.notifyDataSetChanged()
        }
        mEmptyText.isVisible = tasks.isEmpty()
    }

    override fun onClick(itemId: Int) {
        context?.let {
            mStartActivityForResult.launch(
                Intent(
                    it.applicationContext,
                    DetailActivity::class.java
                ).apply {
                    putExtra(ExtraConstants.EXTRA_TASK_ID, itemId)
                })
        }
    }

    override fun onChecked(itemId: Int, checked: Boolean) {
        if (itemId != -1) {
            mListViewModel.checkTask(itemId, checked)
        }
    }

    override fun onDelete(itemId: Int) = mListViewModel.deleteTask(itemId)

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireActivity())
            .unregisterReceiver(mTaskListBroadcastReceiver)
        mCoroutineScope.coroutineContext.cancelChildren()
    }
}