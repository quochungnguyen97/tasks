package com.rose.taskassignmenttest.views.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.data.STATUS_IN_PROGRESS
import com.rose.taskassignmenttest.data.STATUS_NOT_STARTED
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.utils.TaskDataUtils
import com.rose.taskassignmenttest.utils.TimeUtils
import com.rose.taskassignmenttest.viewmodels.DetailViewModel
import com.rose.taskassignmenttest.viewmodels.fakers.FakeTaskDao

class DetailFragment : Fragment() {
    private lateinit var mViewModel: DetailViewModel

    private lateinit var mTitleText: EditText
    private lateinit var mCheck: CheckBox
    private lateinit var mDeadlineText: TextView
    private lateinit var mStatusText: TextView
    private lateinit var mCreateTimeText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView: ")
        val root = inflater.inflate(R.layout.fragment_detail, container, false)

        mTitleText = root.findViewById(R.id.detail_title)
        mCheck = root.findViewById(R.id.detail_check)
        mDeadlineText = root.findViewById(R.id.detail_deadline)
        mStatusText = root.findViewById(R.id.detail_status_text)
        mCreateTimeText = root.findViewById(R.id.detail_create_text)

        mViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]
        mViewModel.setTaskDao(FakeTaskDao())
        mViewModel.getTask().observe(requireActivity()) { task -> updateTask(task) }

        mViewModel.loadTask()

        return root
    }

    override fun onResume() {
        super.onResume()
    }

    private fun updateTask(task: Task) {
        Log.i(TAG, "updateTask: ")
        context?.let {
            mTitleText.setText(task.title)
            mCheck.isChecked = task.completed
            mDeadlineText.text = if (task.deadLine == -1L) it.getString(R.string.no_end_date)
            else TimeUtils.getDateTime(task.deadLine)
            mStatusText.text = TaskDataUtils.getStatusText(it, task.status)
            mCreateTimeText.text = TimeUtils.getDateTime(task.createdTime)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DetailFragment()
        private const val TAG = "DetailFragment"
    }
}