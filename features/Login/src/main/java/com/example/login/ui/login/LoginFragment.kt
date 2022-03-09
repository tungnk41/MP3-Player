package com.example.login.ui.login

import androidx.fragment.app.viewModels
import com.example.core.base.BaseFragment
import com.example.login.R
import com.example.login.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: BaseFragment<FragmentLoginBinding, LoginViewModel>(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()
    override fun getVM() = viewModel
}