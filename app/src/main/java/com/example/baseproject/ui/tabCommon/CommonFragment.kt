package com.example.baseproject.ui.tabCommon

import android.os.Bundle
import android.view.View
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

    private var fragmentCommonBinding : FragmentCommonBinding? = null

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val binding = FragmentCommonBinding.bind(view)
//        fragmentCommonBinding = binding
//    }

    override fun setOnClick() {
        super.setOnClick()

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

    }
}