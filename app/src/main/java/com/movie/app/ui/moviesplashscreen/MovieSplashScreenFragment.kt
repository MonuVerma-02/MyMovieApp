package com.movie.app.ui.moviesplashscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.movie.app.R
import com.movie.app.common.Constants
import com.movie.app.databinding.MovieSplashScreenFragmentBinding
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class MovieSplashScreenFragment : Fragment() {
    private lateinit var binding: MovieSplashScreenFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MovieSplashScreenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialiseUiView()
        initialiseSplashScreen()
    }

    private fun initialiseUiView() {
        binding.circleImageView.animation = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
        binding.textMyMovie.animation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_left)
        binding.textApp.animation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_right)
    }

    private fun initialiseSplashScreen() {
        lifecycleScope.launchWhenStarted {
            delay(Constants.SPLASH_SCREEN_TIMEOUT)
            val action = MovieSplashScreenFragmentDirections.actionSplashScreenToMovieList()
            findNavController().navigate(action)
        }
    }
}