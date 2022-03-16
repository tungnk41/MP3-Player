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
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.d
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

        mainViewModel.isPlaying.observe(this, Observer { isPlaying ->
            if(isPlaying){
                binding.bottomController.visibility = View.VISIBLE
            }
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
            if(destination.id == R.id.tabOnlineMusicFragment || destination.id == R.id.tabLocalMusicFragment) {
                d("addOnDestinationChangedListener " + destination.id)
                binding.tbToolbar.visibility = View.GONE
            }else{
                binding.tbToolbar.visibility = View.VISIBLE
            }
        }
    }
}