package com.rose.taskassignmenttest.views.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.constants.ExtraConstants
import com.rose.taskassignmenttest.utils.PreferenceUtils
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.viewmodels.LoginViewModel
import com.rose.taskassignmenttest.views.common.BaseFragment
import com.rose.taskassignmenttest.views.register.RegisterActivity
import javax.inject.Inject

class LoginFragment : BaseFragment() {

    private lateinit var mUsernameEdt: EditText
    private lateinit var mPasswordEdt: EditText
    private lateinit var mLoginBtn: Button

    @Inject
    lateinit var mViewModel: LoginViewModel

    private val mStartActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(result)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mInjector.inject(this)

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        initViewElements(view)
        initViewModel()

        return view
    }

    private fun initViewElements(rootView: View) {
        mUsernameEdt = rootView.findViewById(R.id.login_username)
        mPasswordEdt = rootView.findViewById(R.id.login_password)

        mLoginBtn = rootView.findViewById(R.id.login_btn)
        mLoginBtn.setOnClickListener { onSubmit() }
        rootView.findViewById<Button>(R.id.register_btn).setOnClickListener { openRegisterScreen() }
    }

    private fun initViewModel() {
        mViewModel.getLoginResult().observe(requireActivity()) { token -> onLoginResponse(token) }
        mViewModel.getUsername().observe(requireActivity()) { username -> mUsernameEdt.setText(username) }
        mViewModel.getPassword().observe(requireActivity()) { password -> mPasswordEdt.setText(password) }
        mViewModel.getIsUpdating().observe(requireActivity()) { isUpdating -> mLoginBtn.isEnabled = !isUpdating }
    }

    private fun onSubmit() {
        val username = mUsernameEdt.text.toString()
        val password = mPasswordEdt.text.toString()

        if (StringUtils.isEmptyOrBlank(username) || StringUtils.isEmptyOrBlank(password)) {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.empty_username_password),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            mViewModel.login(username, password)
        }
    }

    private fun openRegisterScreen() {
        mStartActivityForResult.launch(
            Intent(
                requireContext().applicationContext,
                RegisterActivity::class.java
            )
        )
    }

    private fun onLoginResponse(token: String) {
        activity?.let {
            if (StringUtils.isEmptyOrBlank(token)) {
                Toast.makeText(it, it.getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(it, it.getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                PreferenceUtils.setAccountToken(it, token)
                it.setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(ExtraConstants.EXTRA_RELOAD_LIST, true)
                    putExtra(ExtraConstants.EXTRA_RELOAD_MENU, true)
                    putExtra(ExtraConstants.EXTRA_START_SYNC, true)
                })
                it.finish()
            }
        }
    }

    private fun handleActivityResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val registeredUsername = result.data?.getStringExtra(
                ExtraConstants.EXTRA_REGISTERED_USERNAME
            ) ?: StringUtils.EMPTY

            if (!StringUtils.isEmptyOrBlank(registeredUsername)) {
                mViewModel.updateUsernamePassword(registeredUsername, StringUtils.EMPTY)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.updateUsernamePassword(mUsernameEdt.text.toString(), mPasswordEdt.text.toString())
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}