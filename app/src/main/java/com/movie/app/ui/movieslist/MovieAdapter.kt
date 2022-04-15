package com.movie.app.ui.movieslist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.movie.app.R
import com.movie.app.common.Constants
import com.movie.app.common.RequestOptions
import com.movie.app.model.Movie

class MovieAdapter(private val moviesClickListener: MoviesClickListener) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var data = mutableListOf<Movie?>()

    interface MoviesClickListener {
        fun onMovieClicked(movie: Movie)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<Movie?>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie?) {
            itemView.findViewById<TextView>(R.id.titleText).text = movie?.title
            itemView.findViewById<TextView>(R.id.subTitleText).text = movie?.overview
            itemView.findViewById<RatingBar>(R.id.ratingBar).rating = (movie?.voteAverage!! / 2)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(RequestOptions.defaultRequestOptions())
                .load("${Constants.POSTER_IMAGE_PATH_PREFIX}${movie?.posterPath}")
                .into(itemView.findViewById(R.id.imageView))

            itemView.setOnClickListener {
                movie?.let {
                    moviesClickListener.onMovieClicked(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.movie_list_item,
            parent,
            false
        )

        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}