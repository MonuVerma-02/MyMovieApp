package com.movie.app.ui.movieslist

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.movie.app.R
import com.movie.app.common.Keyboard
import com.movie.app.common.MovieLoadingState
import com.movie.app.databinding.MovieListFragmentBinding
import com.movie.app.model.Movie
import com.movie.app.api.MovieApiService
import com.movie.app.repository.MovieRepository
import com.movie.app.utils.checkConnect
import com.movie.app.utils.extension.hide
import com.movie.app.utils.extension.show
import com.movie.app.utils.textChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject
import androidx.appcompat.view.ContextThemeWrapper as ContextThemeWrapper

@AndroidEntryPoint
class MovieFragment : Fragment(), MovieAdapter.MoviesClickListener {
    private lateinit var binding: MovieListFragmentBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter

    @Inject
    lateinit var factory: MovieViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            MovieListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, factory).get(MovieViewModel::class.java)

        initialiseObservers()
        initialiseCollectors()
        initialiseUiView()
        initialisePopupMenu()
    }

    private fun initialiseObservers() {
        viewModel.movieMediatorLiveData.observe(viewLifecycleOwner, Observer {
            movieAdapter.updateData(it)
        })

        viewModel.movieLoadingStatusLiveData.observe(viewLifecycleOwner, Observer {
            onMovieLoadingStateChanged(it)
        })
        viewModel.navSubHeaderLiveData.observe(viewLifecycleOwner, Observer {
            binding.textSubHeader.text = it
        })
    }

    private fun initialiseCollectors() {
        lifecycleScope.launchWhenStarted {
            activity?.checkConnect()?.collect { isAvailable ->
                when (isAvailable) {
                    true -> {
                        viewModel.onFragmentReady()
                        binding.statusButton.hide()
                        binding.moviesRecyclerView.show()
                        binding.searchEditText.show()
                    }
                    false -> {
                        binding.statusButton.show()
                        binding.moviesRecyclerView.hide()
                        binding.searchEditText.hide()
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            binding.edTextSearch.textChanged().debounce(500L).collect {
                viewModel.onSearchQuery(it.toString())
                viewModel.setNavSubHeader(getString(R.string.search_result))
            }
        }
    }

    private fun initialiseUiView() {
        movieAdapter = MovieAdapter(this)
        binding.moviesRecyclerView.apply {
            adapter = movieAdapter
            hasFixedSize()
        }
    }

    private fun initialisePopupMenu() {
        binding.menuIcon.setOnClickListener {
            val wrapper = ContextThemeWrapper(activity, R.style.PopupMenu)
            val popupMenu = PopupMenu(wrapper, view, Gravity.END)
            popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.current_movie_menu_item -> {
                        viewModel.fetchCurrentMovies()
                        viewModel.setNavSubHeader(getString(R.string.current_movies))
                    }
                    R.id.top_rated_movie_menu_item -> {
                        viewModel.fetchTopRatedMovies()
                        viewModel.setNavSubHeader(getString(R.string.top_rated_movies))
                    }
                    R.id.upcoming_movie_menu_item -> {
                        viewModel.fetchUpcomingMovies()
                        viewModel.setNavSubHeader(getString(R.string.upcoming_movies))
                    }
                    R.id.popular_movie_menu_item -> {
                        viewModel.fetchPopularMovies()
                        viewModel.setNavSubHeader(getString(R.string.popular_movies))
                    }
                }
                Keyboard.hide(requireActivity(), binding.root)
                return@setOnMenuItemClickListener false
            }
            popupMenu.show()
        }
    }

    override fun onMovieClicked(movie: Movie) {
        movie?.let { movie ->
            val action = MovieFragmentDirections.actionMovieListToDetails(movie)
            findNavController().navigate(action)
        }
    }

    private fun onMovieLoadingStateChanged(state: MovieLoadingState) {
        when (state) {
            MovieLoadingState.LOADING -> {
                binding.statusButton.hide()
                binding.moviesRecyclerView.hide()
                binding.loadingProgressBar.show()
            }
            MovieLoadingState.LOADED -> {
                lifecycleScope.launchWhenStarted {
                    activity?.checkConnect()?.collect {
                        if (it) {
                            binding.statusButton.hide()
                            binding.moviesRecyclerView.show()
                        } else {
                            binding.statusButton.show()
                            binding.moviesRecyclerView.hide()
                        }
                    }
                }
                binding.loadingProgressBar.hide()
            }
            MovieLoadingState.ERROR -> {
                binding.statusButton.show()
                binding.moviesRecyclerView.hide()
                binding.loadingProgressBar.hide()

                binding.statusButton.text = getString(R.string.error_message)
                binding.statusButton.setCompoundDrawables(null, null, null, null)
            }
        }

    }

}