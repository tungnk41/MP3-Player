package com.example.baseproject.container

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.baseproject.R
import com.example.baseproject.databinding.ActivityMainBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.bottomController.BottomControllerFragment
import com.example.core.base.BaseActivity
import com.example.core.base.BaseActivityNotRequireViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>() {

    override val layoutId = R.layout.activity_main

    @Inject
    lateinit var appNavigation: AppNavigation

    private val viewModel : MainViewModel by viewModels()
    override fun getVM(): MainViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        appNavigation.bind(navHostFragment.navController)
        checkPermission()

    }

    private fun checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val requestCode = 200
            val perms = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

            var isNeedToRequest = false
            perms.forEach { permission ->
                if(ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED ){
                    isNeedToRequest = true
                }
            }
            if(isNeedToRequest) {
                this.requestPermissions(perms,requestCode)
            }
        }
    }

    override fun onDestroy() {
        appNavigation.unbind()
        super.onDestroy()
    }
}