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
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.constants.ExtraConstants
import com.rose.taskassignmenttest.data.User
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.viewmodels.RegisterViewModel
import com.rose.taskassignmenttest.views.common.BaseFragment
import javax.inject.Inject

class RegisterFragment : BaseFragment() {

    private lateinit var mUsernameTv: TextView
    private lateinit var mDisplayNameTv: TextView
    private lateinit var mPasswordTv: TextView
    private lateinit var mConfirmPasswordTv: TextView
    private lateinit var mRegisterBtn: Button

    @Inject
    lateinit var mViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mInjector.inject(this)

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
            mViewModel.getRegisterResult().observe(act) { userToken ->
                onRegisterResult(!StringUtils.isEmptyOrBlank(userToken))
            }
            mViewModel.getIsUpdating().observe(act) { isUpdating ->
                mRegisterBtn.isEnabled = !isUpdating
            }
            mViewModel.getRegisterStuffs().observe(act) { registerStuffs ->
                mUsernameTv.text = registerStuffs.username
                mDisplayNameTv.text = registerStuffs.displayName
                mPasswordTv.text = registerStuffs.password
                mConfirmPasswordTv.text = registerStuffs.confirmPassword
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
        activity?.let { act ->
            if (isSuccess) {
                Toast.makeText(act, R.string.register_success, Toast.LENGTH_SHORT).show()
                act.setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(ExtraConstants.EXTRA_REGISTERED_USERNAME, mUsernameTv.text.toString())
                })
                act.finish()
            } else {
                Toast.makeText(act, R.string.register_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.updateRegisterStuffs(
            RegisterViewModel.RegisterStuffs(
                mUsernameTv.text.toString(),
                mDisplayNameTv.text.toString(),
                mPasswordTv.text.toString(),
                mConfirmPasswordTv.text.toString()
            )
        )
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }

}