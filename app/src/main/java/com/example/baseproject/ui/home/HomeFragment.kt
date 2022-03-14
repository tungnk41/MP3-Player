package com.example.baseproject.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.baseproject.R
import com.example.baseproject.container.MainViewModel
import com.example.baseproject.databinding.FragmentHomeBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.navigation.HomeNavigation
import com.example.baseproject.ui.bottomController.BottomControllerFragment
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {

    @Inject
    lateinit var appNavigation: AppNavigation

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val viewModel: HomeViewModel by viewModels()
    override fun getVM(): HomeViewModel = viewModel

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.nav_host_container) as NavHostFragment
        homeNavigation.bind(navHostFragment.navController)
        setupBottomNavigationBar()
        setupBottomController()

        mainViewModel.isPlaying.observe(this, Observer { isPlaying ->
            binding.bottomController.visibility = if(isPlaying) View.VISIBLE else View.GONE
        })
    }


    private fun setupBottomController() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.bottom_controller, BottomControllerFragment())
            .commit()
    }

    private fun setupBottomNavigationBar() {
        val navHostFragment = childFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
    }



}