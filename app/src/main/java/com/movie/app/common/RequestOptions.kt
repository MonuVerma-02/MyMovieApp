package com.movie.app.common

import com.bumptech.glide.request.RequestOptions
import com.movie.app.R

object RequestOptions {
    fun defaultRequestOptions(): RequestOptions {
        return RequestOptions()
            .error(R.drawable.ic_movie_placeholder)
            .placeholder(R.drawable.ic_movie_placeholder)
    }
}