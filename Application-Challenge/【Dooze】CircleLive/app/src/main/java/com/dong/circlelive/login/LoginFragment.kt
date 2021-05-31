package com.dong.circlelive.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.dong.circlelive.main.MainActivity
import com.dong.circlelive.R
import com.dong.circlelive.base.BaseFragment
import com.dong.circlelive.base.viewBinding
import com.dong.circlelive.databinding.FragmentLoginBinding
import com.dong.circlelive.start


class LoginFragment : BaseFragment(R.layout.fragment_login), View.OnClickListener {

    private lateinit var loginViewModel: LoginViewModel

    private val binding by viewBinding(FragmentLoginBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        val usernameEditText = binding.etUsername
        val passwordEditText = binding.etPassword
        val loginButton = binding.btnLogic

        loginButton.setOnClickListener(this)

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                binding.btnLogic.isEnabled = true
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    start(MainActivity::class.java, finishActivity = true)
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString().trim(),
                    passwordEditText.text.toString().trim()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEditText.text.toString().trim(),
                    passwordEditText.text.toString().trim()
                )
            }
            false
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_logic -> {
                binding.btnLogic.isEnabled = false
                loginViewModel.login(
                    binding.etUsername.text.toString().trim(),
                    binding.etPassword.text.toString().trim()
                )
            }
        }
    }

    private fun showLoginFailed(errorString: String) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

}