package com.rose.taskassignmenttest.views.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.constants.ExtraConstants
import com.rose.taskassignmenttest.utils.PreferenceUtils
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.viewmodels.AccountViewModel
import com.rose.taskassignmenttest.viewmodels.FailResult

class AccountFragment : Fragment() {

    private lateinit var mViewModel: AccountViewModel

    private lateinit var mUsernameTxt: TextView
    private lateinit var mDisplayNameTxt: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_account, container, false)

        initViewElements(rootView)
        initViewModel()

        mViewModel.fetchUserInfo(PreferenceUtils.getAccountToken(requireContext()))

        return rootView
    }

    private fun initViewElements(rootView: View) {
        mUsernameTxt = rootView.findViewById(R.id.username_txt)
        mDisplayNameTxt = rootView.findViewById(R.id.display_name_txt)
        rootView.findViewById<Button>(R.id.logout_btn).setOnClickListener {
            mViewModel.logout()
        }

        mUsernameTxt.text = requireContext().getString(R.string.username_pattern, StringUtils.EMPTY)
        mDisplayNameTxt.text =
            requireContext().getString(R.string.display_name_pattern, StringUtils.EMPTY)
    }

    private fun initViewModel() {
        val act = requireActivity()
        mViewModel = ViewModelProvider(act)[AccountViewModel::class.java]

        mViewModel.getUser().observe(act) { user ->
            mUsernameTxt.text = act.getString(R.string.username_pattern, user.username)
            mDisplayNameTxt.text = act.getString(R.string.display_name_pattern, user.displayName)
        }

        mViewModel.getLogoutStatus().observe(act) { isLogout ->
            if (isLogout) {
                Toast.makeText(act, R.string.logged_out, Toast.LENGTH_SHORT).show()
                onLogout()
            }
        }

        mViewModel.getFetchDataFailed().observe(act) { failResult -> onFetchDataFailed(failResult) }
    }

    private fun onFetchDataFailed(failResult: FailResult) {
        when (failResult) {
            FailResult.WRONG_DATA -> {
                Toast.makeText(requireActivity(), R.string.user_token_expired, Toast.LENGTH_SHORT).show()
                onLogout()
            }
            FailResult.SERVER_TIMEOUT -> {
                Toast.makeText(requireActivity(), R.string.server_timeout, Toast.LENGTH_SHORT).show()
            }
            FailResult.CONNECTION_FAILED -> {
                Toast.makeText(requireActivity(), R.string.connection_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onLogout() {
        val act = requireActivity()
        PreferenceUtils.removeAccountToken(act)
        act.setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(ExtraConstants.EXTRA_RELOAD_LIST, true)
            putExtra(ExtraConstants.EXTRA_RELOAD_MENU, true)
        })
        act.finish()
    }

    companion object {
        private const val TAG = "AccountFragment"
        fun newInstance() = AccountFragment()
    }
}