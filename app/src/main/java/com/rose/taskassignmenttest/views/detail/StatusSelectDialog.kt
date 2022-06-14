package com.rose.taskassignmenttest.views.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.data.STATUS_DONE
import com.rose.taskassignmenttest.data.STATUS_IN_PROGRESS
import com.rose.taskassignmenttest.data.STATUS_NOT_STARTED
import com.rose.taskassignmenttest.viewmodels.DetailViewModel

class StatusSelectDialog: DialogFragment() {
    private lateinit var mViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_status_select, container, false)

        view.findViewById<View>(R.id.status_select_not_started)
            .setOnClickListener { updateStatus(STATUS_NOT_STARTED) }
        view.findViewById<View>(R.id.status_select_in_progress)
            .setOnClickListener { updateStatus(STATUS_IN_PROGRESS) }
        view.findViewById<View>(R.id.status_select_done)
            .setOnClickListener { updateStatus(STATUS_DONE) }

        mViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        return view
    }

    private fun updateStatus(status: Int) {
        mViewModel.updateStatus(status)
        dismiss()
    }
}