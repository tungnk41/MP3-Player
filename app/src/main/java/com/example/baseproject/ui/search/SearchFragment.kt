package com.example.baseproject.ui.search

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentSearchBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.BaseFragment
import javax.inject.Inject

class SearchFragment : BaseFragment<FragmentSearchBinding,SearchViewModel>(R.layout.fragment_search){

    @Inject
    lateinit var appNavigation: AppNavigation

    private val viewModel: SearchViewModel by viewModels()
    override fun getVM(): SearchViewModel = viewModel
}