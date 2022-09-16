package com.rose.taskassignmenttest.views.detail

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.constants.ExtraConstants
import com.rose.taskassignmenttest.data.STATUS_DONE
import com.rose.taskassignmenttest.data.STATUS_IN_PROGRESS
import com.rose.taskassignmenttest.data.STATUS_NOT_STARTED
import com.rose.taskassignmenttest.data.Task
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.utils.TimeUtils
import com.rose.taskassignmenttest.utils.ViewUtils
import com.rose.taskassignmenttest.viewmodels.DetailViewModel
import com.rose.taskassignmenttest.views.common.BaseFragment
import com.rose.taskassignmenttest.views.common.StatusTagView
import javax.inject.Inject

class DetailFragment : BaseFragment() {
    @Inject
    lateinit var mViewModel: DetailViewModel

    private lateinit var mTitleText: EditText
    private lateinit var mCheck: CheckBox
    private lateinit var mDeadlineText: TextView
    private lateinit var mStatusTagView: StatusTagView
    private lateinit var mCreateTimeText: TextView
    private lateinit var mModifiedTimeText: TextView

    private lateinit var mStatusPopupMenu: PopupMenu

    private lateinit var mDeadlineTimeHandler: DateTimeHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mInjector.inject(this)

        val root = inflater.inflate(R.layout.fragment_detail, container, false)

        initViewElements(root)

        initViewModel()

        return root
    }

    private fun initViewElements(root: View) {
        mTitleText = root.findViewById(R.id.detail_title)
        mCheck = root.findViewById(R.id.detail_check)
        mDeadlineText = root.findViewById(R.id.detail_deadline)
        mStatusTagView = root.findViewById(R.id.detail_status_tag)
        mCreateTimeText = root.findViewById(R.id.detail_create_text)
        mModifiedTimeText = root.findViewById(R.id.detail_modified_text)

        val deadlineContainer: View = root.findViewById(R.id.detail_deadline_container)
        val statusContainer: View = root.findViewById(R.id.detail_status_container)
        val cancelButton: Button = root.findViewById(R.id.detail_cancel_btn)
        val saveButton: Button = root.findViewById(R.id.detail_save_btn)

        mDeadlineTimeHandler = DateTimeHandler(requireContext()) {
                time -> mViewModel.updateDeadline(time)
        }

        mStatusPopupMenu = PopupMenu(requireContext(), statusContainer).apply {
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_not_started -> {
                        mViewModel.updateStatus(STATUS_NOT_STARTED)
                        true
                    }
                    R.id.menu_in_progress -> {
                        mViewModel.updateStatus(STATUS_IN_PROGRESS)
                        true
                    }
                    R.id.menu_done -> {
                        mViewModel.updateStatus(STATUS_DONE)
                        true
                    }
                    else -> false
                }
            }
            inflate(R.menu.detail_status_popup_menu)
        }

        deadlineContainer.setOnClickListener {
            mTitleText.clearFocus()
            ViewUtils.hideInputMethod(requireContext(), root)
            mDeadlineTimeHandler.showDialogs()
        }

        statusContainer.setOnClickListener {
            mStatusPopupMenu.show()
        }

        saveButton.setOnClickListener {
            if (StringUtils.isEmptyOrBlank(mTitleText.text.toString())) {
                warnTitleEmpty()
            } else {
                mViewModel.saveTask(mTitleText.text.toString(), mCheck.isChecked)
            }
        }

        cancelButton.setOnClickListener {
            mViewModel.checkDataChanged(mTitleText.text.toString(), mCheck.isChecked)
        }
    }

    private fun initViewModel() {
        activity?.let {
            mViewModel.getTask().observe(it) { task -> updateTask(task) }
            mViewModel.getDynamicDetailData().observe(it) { dynamicData ->
                mCheck.isChecked = dynamicData.completed
                mTitleText.setText(dynamicData.title)
                mTitleText.setSelection(dynamicData.title.length)
            }
            mViewModel.getIsSaveSuccess().observe(it) { isSaved -> onTaskSaved(isSaved) }
            mViewModel.getIsDataChanged().observe(it) { isDataChanged ->
                onDataChanged(isDataChanged)
            }
        }
    }

    private fun warnTitleEmpty() {
        context?.let {
            Toast.makeText(it, it.getString(R.string.title_empty_notify), Toast.LENGTH_SHORT)
                .show()
            mTitleText.requestFocus()
        }
    }

    private fun updateTask(task: Task) {
        context?.let {
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
            it.setResult(
                Activity.RESULT_OK,
                Intent().apply { putExtra(ExtraConstants.EXTRA_RELOAD_LIST, true) }
            )
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

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.updateDynamicDetailData(
            mTitleText.text.toString(),
            mCheck.isChecked
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = DetailFragment()
        private const val TAG = "DetailFragment"
    }
}