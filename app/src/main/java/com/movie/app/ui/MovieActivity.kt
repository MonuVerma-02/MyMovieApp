package com.movie.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.movie.app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_activity)
    }
}