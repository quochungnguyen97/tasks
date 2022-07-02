package com.rose.taskassignmenttest.views.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.viewmodels.ListViewModel
import com.rose.taskassignmenttest.constants.ExtraConstants
import com.rose.taskassignmenttest.constants.PreferenceConstants
import com.rose.taskassignmenttest.utils.PreferenceUtils
import com.rose.taskassignmenttest.views.detail.DetailActivity
import com.rose.taskassignmenttest.views.list.items.ItemsSorter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TaskListFragment : Fragment(), TaskListListener {
    private lateinit var mListAdapter: ListAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mEmptyText: TextView

    private lateinit var mListViewModel: ListViewModel

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

        mRecyclerView = root.findViewById(R.id.list)
        mEmptyText = root.findViewById(R.id.empty_text)

        activity?.let {
            mListAdapter = ListAdapter(it, this)
            mRecyclerView.layoutManager = LinearLayoutManager(it)
            mRecyclerView.adapter = mListAdapter
            mListViewModel = ViewModelProvider(it).get(ListViewModel::class.java)
            mListViewModel.loadAllTasks()
            mListViewModel.getAllTasks().observe(it) { tasks -> updateTasks(tasks) }
        }

        setHasOptionsMenu(true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.let {
            it.menuInflater.inflate(R.menu.task_list_option_menu, menu)
            if (PreferenceUtils.getBooleanPreference(
                    it,
                    PreferenceConstants.PREF_KEY_HIDE_COMPLETED
                )
            ) {
                menu.findItem(R.id.task_list_menu_item_hide_completed)?.isVisible = false
            } else {
                menu.findItem(R.id.task_list_menu_item_show_completed)?.isVisible = false
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
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
            else -> super.onOptionsItemSelected(item)
        }

    private fun updateHideCompleted(hideCompleted: Boolean) {
        activity?.let {
            PreferenceUtils.setPreference(it, PreferenceConstants.PREF_KEY_HIDE_COMPLETED, hideCompleted)
            mListViewModel.loadAllTasks()
            it.invalidateOptionsMenu()
        }

    }

    override fun onResume() {
        super.onResume()
        mListViewModel.loadAllTasks()
    }

    private fun updateTasks(tasks: MutableList<Task>) {
        context?.let {
            CoroutineScope(Dispatchers.IO).launch {
                mListAdapter.updateItems(
                    ItemsSorter.getGroupSortItemsList(
                        tasks,
                        ItemsSorter.GROUP_BY_STATUS,
                        it
                    )
                )
                CoroutineScope(Dispatchers.Main).launch { mListAdapter.notifyDataSetChanged() }
            }
        }
        mEmptyText.isVisible = tasks.isEmpty()
    }

    override fun onClick(itemId: Int) {
        context?.let {
            it.startActivity(Intent(it.applicationContext, DetailActivity::class.java).apply {
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
}