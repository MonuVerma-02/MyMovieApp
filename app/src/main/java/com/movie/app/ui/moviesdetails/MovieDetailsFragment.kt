package com.movie.app.ui.moviesdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.movie.app.R
import com.movie.app.common.Constants
import com.movie.app.common.MovieLoadingState
import com.movie.app.common.RequestOptions
import com.movie.app.databinding.MovieDetailsFragementBinding
import com.movie.app.model.Video
import com.movie.app.api.MovieApiService
import com.movie.app.repository.MovieDetailsRepository
import com.movie.app.utils.checkConnect
import com.movie.app.utils.extension.empty
import com.movie.app.utils.extension.hide
import com.movie.app.utils.extension.show
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private lateinit var binding: MovieDetailsFragementBinding
    private val args: MovieDetailsFragmentArgs by navArgs()
    private lateinit var viewModelDetails: MovieDetailsViewModel

    @Inject
    lateinit var factory: MovieDetailsViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MovieDetailsFragementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelDetails = ViewModelProvider(this, factory).get(MovieDetailsViewModel::class.java)

        initialiseUiView()
        initialiseObservers()
        initialiseCollectors()
    }

    private fun initialiseUiView() {
        binding.titleText.text = args.movie.title
        binding.subTitleText.text = args.movie.overview
        binding.ratingBar.rating = (args.movie.voteAverage!! / 2)
        binding.popularityText.text =
            getString(R.string.popularity).plus("\n ${args.movie.popularity.toString()}")
        binding.dateText.text =
            getString(R.string.release_date).plus("\n ${args.movie.releaseDate.toString()}")

        Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions.defaultRequestOptions())
            .load("${Constants.POSTER_IMAGE_PATH_PREFIX}${args.movie?.posterPath}")
            .into(binding.imageView)
    }

    private fun initialiseObservers() {
        viewModelDetails.videoLoadingStatusLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                MovieLoadingState.LOADED -> {
                    binding.errorText.hide()
                    binding.errorText.text = String.empty()
                }
                MovieLoadingState.ERROR -> {
                    binding.errorText.show()
                    binding.errorText.text = getString(R.string.error_message)
                }
            }
        })
    }

    private fun initialiseCollectors() {
        lifecycleScope.launchWhenStarted {
            activity?.checkConnect()?.collect { isAvailable ->
                when (isAvailable) {
                    true -> {
                        viewModelDetails.fetchMovieVideosById(args.movie.id!!)
                            .observe(viewLifecycleOwner, Observer {
                                onPlayVideoTrailer(it)
                            })
                        binding.errorText.hide()
                        binding.errorText.text = String.empty()
                    }
                    false -> {
                        binding.errorText.show()
                        binding.errorText.text = getString(R.string.no_internet_connection)
                    }
                }

            }
        }
    }

    private fun onPlayVideoTrailer(videos: List<Video>) {
        val video = videos.filter { it.type == Constants.MOVIE_TYPE }
            .map { it.key }

        lifecycle.addObserver(binding.youtubePlayer)
        binding.youtubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                if (video.isNotEmpty()) {
                    youTubePlayer.cueVideo(video[0], 0f)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.youtubePlayer.release()
    }

}