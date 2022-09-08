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
import androidx.lifecycle.ViewModelProvider
import com.rose.taskassignmenttest.R
import com.rose.taskassignmenttest.constants.ExtraConstants
import com.rose.taskassignmenttest.utils.PreferenceUtils
import com.rose.taskassignmenttest.utils.StringUtils
import com.rose.taskassignmenttest.viewmodels.LoginViewModel

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        activity?.let {
            val viewModel = ViewModelProvider(it)[LoginViewModel::class.java]
            viewModel.getLoginResult().observe(it) { token -> onLoginResponse(token) }

            val usernameEdt = view.findViewById<EditText>(R.id.login_username)
            val passwordEdt = view.findViewById<EditText>(R.id.login_password)

            val loginBtn = view.findViewById<Button>(R.id.login_btn)
            loginBtn.setOnClickListener { _ ->
                val username = usernameEdt.text.toString()
                val password = passwordEdt.text.toString()

                if (StringUtils.isEmptyOrBlank(username) || StringUtils.isEmptyOrBlank(password)) {
                    Toast.makeText(it, it.getString(R.string.empty_username_password), Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.login(username, password)
                }
            }
        }

        return view
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

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}