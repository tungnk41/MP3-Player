package com.example.baseproject.ui.mainPlaying

import android.os.Bundle
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.container.MainViewModel
import com.example.baseproject.databinding.FragmentMainPlayingBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.utils.PROGRESS_BAR_MAX_VALUE
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber.d
import javax.inject.Inject

@AndroidEntryPoint
class MainPlayingFragment : BaseFragment<FragmentMainPlayingBinding,MainPlayingViewModel>(R.layout.fragment_main_playing) {

    @Inject
    lateinit var appNavigation: AppNavigation

    private val viewModel: MainPlayingViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private var isPlayingUI: Boolean = false

    override fun getVM(): MainPlayingViewModel = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        binding.sbSeekBarMainPlaying.max = PROGRESS_BAR_MAX_VALUE


        mainViewModel.currentProgress.observe(this, Observer {
            binding.sbSeekBarMainPlaying.progress = it
        })

        mainViewModel.mediaMetadata.observe(this, Observer {
            binding.tvTitleMainPlaying.text = it.description.title
            binding.tvSubTitleMainPlaying.text = it.description.subtitle

            Glide.with(binding.root)
                .load(it.description.iconUri)
                .centerCrop()
                .into(binding.imgArtworkMainPlaying)
        })

        mainViewModel.isPlaying.observe(this, Observer { isPlaying ->
            if(isPlaying){
                binding.btnPlayPauseMainPlaying.setImageResource(R.drawable.ic_pause_24)
                mainViewModel.startUpdateProgress()
            } else {
                binding.btnPlayPauseMainPlaying.setImageResource(R.drawable.ic_play_24)
                mainViewModel.stopUpdateProgress()
            }
            isPlayingUI = isPlaying
        })
    }

    override fun setOnClick() {
        super.setOnClick()

        binding.btnPrevMainPlaying.setOnClickListener {
            mainViewModel.prev()
        }

        binding.btnPlayPauseMainPlaying.setOnClickListener {
            mainViewModel.playPauseToggle()
        }

        binding.btnNextMainPlaying.setOnClickListener {
            mainViewModel.next()
        }

        binding.sbSeekBarMainPlaying.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    mainViewModel.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mainViewModel.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mainViewModel.play()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if(isPlayingUI){
            mainViewModel.startUpdateProgress()
        }
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.stopUpdateProgress()
    }
}