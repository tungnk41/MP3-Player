package com.example.baseproject.ui.bottomController

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.container.MainViewModel
import com.example.baseproject.databinding.FragmentBottomControllerBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.utils.PROGRESS_BAR_MAX_VALUE
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BottomControllerFragment : BaseFragment<FragmentBottomControllerBinding,BottomControllerViewModel>(R.layout.fragment_bottom_controller) {

    private val viewModel: BottomControllerViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    override fun getVM(): BottomControllerViewModel = viewModel

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        binding.pbProgressBarBottomController.max = PROGRESS_BAR_MAX_VALUE

        mainViewModel.currentProgress.observe(this, Observer {
            binding.pbProgressBarBottomController.progress = it
        })

        mainViewModel.mediaMetadata.observe(this, Observer {
            binding.tvTitleBottomController.text = it.description.title
            binding.tvSubTitleBottomController.text = it.description.subtitle

            Glide.with(binding.root)
                .load(it.description.iconUri)
                .centerCrop()
                .into(binding.imgArtworkBottomController)
        })

        mainViewModel.isPlaying.observe(this, Observer { isPlaying ->
            if(isPlaying){
                binding.btnPlayPauseBottomController.setImageResource(R.drawable.ic_pause_24)
                mainViewModel.startUpdateProgress()
            } else {
                binding.btnPlayPauseBottomController.setImageResource(R.drawable.ic_play_24)
                mainViewModel.stopUpdateProgress()
            }
        })
    }

    override fun setOnClick() {
        super.setOnClick()

        binding.root.setOnClickListener {
            appNavigation.openHomeScreenToPlayingScreen()
        }

        binding.btnPrevBottomController.setOnClickListener {
            mainViewModel.prev()
        }

        binding.btnPlayPauseBottomController.setOnClickListener {
            mainViewModel.playPauseToggle()
        }

        binding.btnNextBottomController.setOnClickListener {
            mainViewModel.next()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}