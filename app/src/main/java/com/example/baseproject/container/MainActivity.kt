package com.example.baseproject.container

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.baseproject.R
import com.example.baseproject.databinding.ActivityMainBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.BaseActivityNotRequireViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.jar.Manifest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivityNotRequireViewModel<ActivityMainBinding>() {

    @Inject
    lateinit var appNavigation: AppNavigation

    override val layoutId = R.layout.activity_main

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        appNavigation.bind(navHostFragment.navController)

        checkPremission()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPremission() {
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