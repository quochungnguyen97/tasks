package com.rose.taskassignmenttest.views.register

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
import com.rose.taskassignmenttest.data.User
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.viewmodels.RegisterViewModel

class RegisterFragment : Fragment() {

    private lateinit var mUsernameTv: TextView
    private lateinit var mDisplayNameTv: TextView
    private lateinit var mPasswordTv: TextView
    private lateinit var mConfirmPasswordTv: TextView
    private lateinit var mRegisterBtn: Button

    private lateinit var mViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_register, container, false)

        initViewElements(rootView)
        initViewModel()

        return rootView
    }

    private fun initViewElements(rootView: View) {
        mUsernameTv = rootView.findViewById(R.id.register_username)
        mPasswordTv = rootView.findViewById(R.id.register_password)
        mConfirmPasswordTv = rootView.findViewById(R.id.register_confirm_password)
        mDisplayNameTv = rootView.findViewById(R.id.register_display_name)

        mRegisterBtn = rootView.findViewById(R.id.register_btn)
        mRegisterBtn.setOnClickListener { onSubmit() }
    }

    private fun initViewModel() {
        activity?.let { act ->
            mViewModel = ViewModelProvider(act)[RegisterViewModel::class.java]
            mViewModel.getRegisterResult().observe(act) { userToken ->
                onRegisterResult(!StringUtils.isEmptyOrBlank(userToken))
            }

        }
    }

    private fun onSubmit() {
        val username = mUsernameTv.text.toString()
        if (StringUtils.isEmptyOrBlank(username)) {
            Toast.makeText(requireContext(), R.string.username_empty, Toast.LENGTH_SHORT).show()
            return
        }

        val displayName = mDisplayNameTv.text.toString()
        if (StringUtils.isEmptyOrBlank(displayName)) {
            Toast.makeText(requireContext(), R.string.display_name_empty, Toast.LENGTH_SHORT).show()
            return
        }

        val password = mPasswordTv.text.toString()
        val confirmPassword = mConfirmPasswordTv.text.toString()

        if (password != confirmPassword) {
            Toast.makeText(requireContext(), R.string.password_not_match, Toast.LENGTH_SHORT).show()
            return
        }
        if (StringUtils.isEmptyOrBlank(password)) {
            Toast.makeText(requireContext(), R.string.password_empty, Toast.LENGTH_SHORT).show()
            return
        }

        mViewModel.register(User(username, password, displayName))
    }

    private fun onRegisterResult(isSuccess: Boolean) {
        if (isSuccess) {
            Toast.makeText(requireContext(), R.string.register_success, Toast.LENGTH_SHORT).show()
            activity?.let { act ->
                act.setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(ExtraConstants.EXTRA_REGISTERED_USERNAME, mUsernameTv.text.toString())
                })
                act.finish()
            }
        } else {
            Toast.makeText(requireContext(), R.string.register_failed, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }

}