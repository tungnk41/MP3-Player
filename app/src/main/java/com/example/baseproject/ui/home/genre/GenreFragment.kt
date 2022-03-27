package com.example.baseproject.ui.home.genre

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentGenreBinding
import com.example.baseproject.navigation.HomeNavigation
import com.example.baseproject.ui.adapter.MediaItemHorizontalAdapter
import com.example.baseproject.ui.adapter.MediaItemVerticalAdapter
import com.example.core.base.BaseFragment
import com.example.mediaservice.repository.models.MediaIdExtra
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class GenreFragment : BaseFragment<FragmentGenreBinding, GenreViewModel>(R.layout.fragment_genre) {

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val viewModel by viewModels<GenreViewModel>()
    override fun getVM(): GenreViewModel = viewModel

    private var parentMediaIdExtra: MediaIdExtra? = null

    private val mAdapter: MediaItemHorizontalAdapter by lazy {
        MediaItemHorizontalAdapter(
            requireContext(),
            onClickListener = { position ->
                Timber.d("Position $position")
                val mediaIdExtra = viewModel.mediaItems.value?.get(position)?.mediaIdExtra
                val title = viewModel.mediaItems.value?.get(position)?.title
                mediaIdExtra?.let {
                    val bundle = Bundle()
                    bundle.putParcelable("mediaIdExtra",it)
                    bundle.putString("title",title)
                    homeNavigation.openGenreScreenToGenreDetailScreen(bundle)
                }
            })
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        parentMediaIdExtra = args?.getParcelable<MediaIdExtra>("mediaIdExtra")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentMediaIdExtra?.let {
            viewModel.startLoadingData(it)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

//        binding.rvListGenre.setHasFixedSize(false)
//        if(!mAdapter.hasObservers()){
//            mAdapter.setHasStableIds(true)
//        }
        (binding.rvListGenre.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvListGenre.adapter = mAdapter

    }

    override fun bindingStateView() {
        super.bindingStateView()

        viewModel.mediaItems.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })
    }

    override fun onDestroyView() {
        binding.rvListGenre.adapter = null
        super.onDestroyView()
    }
}