package com.rose.taskassignmenttest.views.detail

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.data.STATUS_DONE
import com.rose.taskassignmenttest.data.STATUS_IN_PROGRESS
import com.rose.taskassignmenttest.data.STATUS_NOT_STARTED
import com.rose.taskassignmenttest.viewmodels.DetailViewModel

class StatusSelectDialog: DialogFragment() {
    companion object {
        const val TAG = "StatusSelectDialog"
    }

    private lateinit var mViewModel: DetailViewModel

    private var mPositionX = -1
    private var mPositionY = -1

    fun setPosition(x: Int, y: Int) {
        mPositionX = x
        mPositionY = y
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        updatePosition()

        mViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        val view: View = inflater.inflate(R.layout.dialog_status_select, container, false)
        view.findViewById<View>(R.id.status_select_not_started)
            .setOnClickListener { updateStatus(STATUS_NOT_STARTED) }
        view.findViewById<View>(R.id.status_select_in_progress)
            .setOnClickListener { updateStatus(STATUS_IN_PROGRESS) }
        view.findViewById<View>(R.id.status_select_done)
            .setOnClickListener { updateStatus(STATUS_DONE) }

        return view
    }

    private fun updatePosition() {
        dialog?.window?.let {
            if (mPositionX != -1 && mPositionY != -1) {
                it.setGravity(Gravity.TOP or Gravity.START)
                val params = it.attributes
                Log.i(TAG, "updatePosition: (x, y) = ($mPositionX, $mPositionY)")
                params.x = mPositionX
                params.y = mPositionY
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                it.attributes = params
            }
        }
    }

    private fun updateStatus(status: Int) {
        mViewModel.updateStatus(status)
        dismiss()
    }
}