package com.example.baseproject.ui.mainPlaying

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
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

        mainViewModel.currentPositionUI.observe(this, Observer {
            binding.tvPosition.text = it
        })

        mainViewModel.durationUI.observe(this, Observer {
            binding.tvDuration.text = it
        })

        mainViewModel.repeatMode.observe(this, Observer { repeatMode ->
            when(repeatMode) {
                PlaybackStateCompat.REPEAT_MODE_NONE  -> {
                    binding.btnRepeatMainPlaying.setImageResource(R.drawable.ic_repeat_none_24)
                }
                PlaybackStateCompat.REPEAT_MODE_ALL -> {
                    binding.btnRepeatMainPlaying.setImageResource(R.drawable.ic_repeat_all_24)
                }
                PlaybackStateCompat.REPEAT_MODE_ONE -> {
                    binding.btnRepeatMainPlaying.setImageResource(R.drawable.ic_repeat_one_24)
                }
            }
        })

        mainViewModel.shuffleMode.observe(this, Observer { shuffleMode ->
            when(shuffleMode) {
                PlaybackStateCompat.SHUFFLE_MODE_NONE  -> {
                    binding.btnShuffleMainPlaying.setImageResource(R.drawable.ic_shuffle_none_24)
                }
                PlaybackStateCompat.SHUFFLE_MODE_ALL -> {
                    binding.btnShuffleMainPlaying.setImageResource(R.drawable.ic_shuffle_all_24)
                }
            }
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

        binding.btnRepeatMainPlaying.setOnClickListener {
            mainViewModel.nextRepeatMode()
        }

        binding.btnShuffleMainPlaying.setOnClickListener {
            mainViewModel.nextShuffleMode()
        }
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