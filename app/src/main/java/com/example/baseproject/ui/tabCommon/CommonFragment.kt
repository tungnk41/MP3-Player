package com.example.baseproject.ui.tabCommon

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentCommonBinding
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommonFragment :
    BaseFragment<FragmentCommonBinding, CommonViewModel>(R.layout.fragment_common) {

    private val viewModel: CommonViewModel by viewModels()

    override fun getVM(): CommonViewModel = viewModel

}