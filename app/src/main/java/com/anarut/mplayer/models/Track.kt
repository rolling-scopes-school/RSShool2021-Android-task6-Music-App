package com.anarut.mplayer.models

data class Track(
    val mediaId: String,
    val title: String,
    val artist: String,
    val bitmapUri: String,
    val trackUri: String,
    val duration: Int
)
