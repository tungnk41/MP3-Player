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

        binding.btnConnect.setOnClickListener {
            viewModel.connect()
        }

        binding.btnPrev.setOnClickListener {
            viewModel.prev()
        }

        binding.btnPlay.setOnClickListener {
            viewModel.play()
        }

        binding.btnNext.setOnClickListener {
            viewModel.next()
        }

        binding.btnSearch.setOnClickListener {
            viewModel.search()
        }

        binding.btnCmd.setOnClickListener {
            viewModel.sendCommand()
        }

        binding.btnSendPlaylist.setOnClickListener {
            viewModel.sendPlaylist()
        }

        binding.btnGetPlaylist.setOnClickListener {
            viewModel.getPlaylist()
        }

        binding.btnRepeate.setOnClickListener {
            viewModel.repeate()
        }

        binding.btnShuffle.setOnClickListener {
            viewModel.shuffle()
        }

        binding.btnSeekTo.setOnClickListener {
            viewModel.seekTo()
        }
    }
}