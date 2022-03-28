package com.example.baseproject.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.baseproject.R
import com.example.baseproject.container.MainViewModel
import com.example.baseproject.databinding.FragmentHomeBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.navigation.HomeNavigation
import com.example.baseproject.ui.bottomController.BottomControllerFragment
import com.example.core.base.BaseFragment
import com.example.core.base.BaseFragmentNotRequireViewModel
import com.example.core.base.BaseViewModel
import com.example.mediaservice.extensions.EMPTY_MEDIA_METADATA_COMPAT
import com.example.mediaservice.extensions.EMPTY_PLAYBACK_STATE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding,HomeViewModel>(R.layout.fragment_home) {

    @Inject
    lateinit var appNavigation: AppNavigation
    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel by viewModels<HomeViewModel>()
    override fun getVM(): HomeViewModel = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.nav_host_container) as NavHostFragment
        homeNavigation.bind(navHostFragment.navController)
        setupBottomNavigationBar()
        setupBottomController()

    }

    override fun bindingStateView() {
        super.bindingStateView()

        mainViewModel.mediaMetadata.observe(this, Observer { metadata ->
            viewModel.setCurrentMetadata(metadata)
        })

        viewModel.mIsShowBottomController.observe(this, Observer {
            binding.bottomController.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.mIsShowBottomNav.observe(this, Observer {
            binding.bottomNav.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.mIsShowToolbar.observe(this, Observer {
            binding.tbToolbar.visibility = if(it) View.VISIBLE else View.GONE
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
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.bottomNav.setupWithNavController(navController)
        binding.tbToolbar.setupWithNavController(navController,appBarConfiguration)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            viewModel.handleConditionScreenId(destination.id)
        }
    }

    override fun onDestroyView() {
        homeNavigation.unbind()
        super.onDestroyView()
    }


}