package com.example.baseproject.ui.login

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentLoginBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.splash.SplashViewModel
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding,LoginViewModel>(R.layout.fragment_login) {

    @Inject
    lateinit var appNavigation: AppNavigation

    private val viewModel: LoginViewModel by viewModels()

    override fun getVM() = viewModel

    override fun setOnClick() {
        super.setOnClick()

        binding.btnLogin.setOnClickListener {
            appNavigation.openLoginToHomeScreen()
        }

    }
}