package com.rose.taskassignmenttest.views.detail

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.utils.TimeUtils
import com.rose.taskassignmenttest.viewmodels.DetailViewModel
import com.rose.taskassignmenttest.viewmodels.fakers.FakeTaskDao
import com.rose.taskassignmenttest.views.common.StatusTagView
import java.util.*

class DetailFragment : Fragment(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {
    private lateinit var mViewModel: DetailViewModel

    private lateinit var mTitleText: EditText
    private lateinit var mCheck: CheckBox
    private lateinit var mDeadlineText: TextView
    private lateinit var mStatusTagView: StatusTagView
    private lateinit var mCreateTimeText: TextView
    private lateinit var mModifiedTimeText: TextView

    private val mDeadlineCalendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_detail, container, false)

        mTitleText = root.findViewById(R.id.detail_title)
        mCheck = root.findViewById(R.id.detail_check)
        mDeadlineText = root.findViewById(R.id.detail_deadline)
        mStatusTagView = root.findViewById(R.id.detail_status_tag)
        mCreateTimeText = root.findViewById(R.id.detail_create_text)
        mModifiedTimeText = root.findViewById(R.id.detail_modified_text)
        val deadlineContainer: View = root.findViewById(R.id.detail_deadline_container)
        val statusContainer: View = root.findViewById(R.id.detail_status_container)
        val cancelButton: Button = root.findViewById(R.id.detail_cancel_btn)

        activity?.let {
            mViewModel = ViewModelProvider(it)[DetailViewModel::class.java]
            mViewModel.setTaskDao(FakeTaskDao())
            mViewModel.getTask().observe(it) { task -> updateTask(task) }
            mViewModel.getIsSaveSuccess().observe(it) { isSaved -> onTaskSaved(isSaved) }
            mViewModel.getIsDataChanged().observe(it) { isDataChanged ->
                onDataChanged(isDataChanged)
            }

            mViewModel.loadTask()
            root?.let { view ->
                view.findViewById<Button>(R.id.detail_save_btn)
                    .setOnClickListener {
                        updateTitleAndChecked()
                        mViewModel.saveTask()
                    }
            }
        }

        statusContainer.setOnClickListener {
            val selectDialog = StatusSelectDialog()

            val locationWindow = intArrayOf(-1, -1)
            mStatusTagView.getLocationInWindow(locationWindow)

            selectDialog.setPosition(
                locationWindow[0] - mStatusTagView.width / 2,
                locationWindow[1] - mStatusTagView.height / 2
            )
            selectDialog.show(requireActivity().supportFragmentManager, StatusSelectDialog.TAG)
        }

        deadlineContainer.setOnClickListener {
            mTitleText.clearFocus()
            val currentCalendar = Calendar.getInstance()
            currentCalendar.timeInMillis = System.currentTimeMillis()
            DatePickerDialog(
                requireContext(), this, currentCalendar.get(Calendar.YEAR),
                currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        cancelButton.setOnClickListener {
            updateTitleAndChecked()
            mViewModel.checkDataChanged()
        }

        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        updateTitleAndChecked()
    }

    private fun updateTitleAndChecked() {
        mViewModel.updateTaskData(mTitleText.text.toString(), mCheck.isChecked)
    }

    private fun updateTask(task: Task) {
        context?.let {
            mTitleText.setText(task.title)
            mTitleText.setSelection(task.title.length)
            mCheck.isChecked = task.completed
            mDeadlineText.text = if (task.deadLine == -1L) it.getString(R.string.no_end_date)
            else TimeUtils.getDateTime(task.deadLine)
            mStatusTagView.setStatus(task.status)
            mCreateTimeText.text =
                it.getString(R.string.created_time, TimeUtils.getDateTime(task.createdTime))
            mModifiedTimeText.text =
                it.getString(R.string.last_modified_time, TimeUtils.getDateTime(task.modifiedTime))
        }
    }

    private fun onTaskSaved(isSaved: Boolean) {
        activity?.let {
            Toast.makeText(
                it, it.getString(if (isSaved) R.string.task_saved else R.string.failed_to_save),
                Toast.LENGTH_SHORT
            ).show()
            it.finish()
        }
    }

    private fun onDataChanged(isDataChanged: Boolean) {
        activity?.let {
            if (isDataChanged) {
                AlertDialog.Builder(it).setMessage(R.string.detail_dialog_message)
                    .setPositiveButton(R.string.yes) { _, _ -> it.finish() }
                    .setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            } else {
                it.finish()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DetailFragment()
        private const val TAG = "DetailFragment"
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        mDeadlineCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        mDeadlineCalendar.set(Calendar.MINUTE, minute)

        mViewModel.updateDeadline(mDeadlineCalendar.timeInMillis)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mDeadlineCalendar.set(Calendar.YEAR, year)
        mDeadlineCalendar.set(Calendar.MONTH, month)
        mDeadlineCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val currentCalendar = Calendar.getInstance()
        currentCalendar.timeInMillis = System.currentTimeMillis()

        TimePickerDialog(
            requireContext(), this, currentCalendar.get(Calendar.HOUR_OF_DAY),
            currentCalendar.get(Calendar.MINUTE), true
        ).show()
    }
}