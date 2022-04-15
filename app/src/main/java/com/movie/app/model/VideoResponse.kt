package com.movie.app.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoResponse(
    val id: Int,
    @Json(name = "results")
    val video: List<Video>
)