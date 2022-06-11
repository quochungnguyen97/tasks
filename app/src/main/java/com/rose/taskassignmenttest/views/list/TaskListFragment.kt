package com.rose.taskassignmenttest.views.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.viewmodels.ListViewModel
import com.rose.taskassignmenttest.viewmodels.fakers.FakeTaskDao
import com.rose.taskassignmenttest.constants.ExtraConstants
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
            mListViewModel.setTaskDao(FakeTaskDao())
            mListViewModel.loadAllTasks()
            mListViewModel.getAllTasks().observe(it) { tasks -> updateTasks(tasks) }
        }

        return root
    }

    private fun updateTasks(tasks: MutableList<Task>) {
        Log.i(TAG, "updateTasks: ")
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
}