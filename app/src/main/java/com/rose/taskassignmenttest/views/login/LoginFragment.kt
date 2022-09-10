package com.rose.taskassignmenttest.views.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.constants.ExtraConstants
import com.rose.taskassignmenttest.utils.PreferenceUtils
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.viewmodels.LoginViewModel
import com.rose.taskassignmenttest.views.register.RegisterActivity

class LoginFragment : Fragment() {

    private lateinit var mUsernameEdt: EditText
    private lateinit var mPasswordEdt: EditText

    private lateinit var mViewModel: LoginViewModel

    private val mStartActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(result)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        initViewElements(view)
        initViewModels()

        return view
    }

    private fun initViewElements(rootView: View) {
        mUsernameEdt = rootView.findViewById(R.id.login_username)
        mPasswordEdt = rootView.findViewById(R.id.login_password)

        rootView.findViewById<Button>(R.id.login_btn).setOnClickListener { onSubmit() }
        rootView.findViewById<Button>(R.id.register_btn).setOnClickListener { openRegisterScreen() }
    }

    private fun initViewModels() {
        mViewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        mViewModel.getLoginResult().observe(requireActivity()) { token -> onLoginResponse(token) }
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
                mUsernameEdt.setText(registeredUsername)
                mPasswordEdt.setText(StringUtils.EMPTY)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}