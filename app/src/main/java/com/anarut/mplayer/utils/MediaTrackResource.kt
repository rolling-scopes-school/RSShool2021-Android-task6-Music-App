package com.anarut.mplayer.utils

import android.content.Context
import com.anarut.mplayer.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MediaTrackResource(private val context: Context) {
    fun loadTracks(): List<Track> {
        val jsonFileString = getJsonDataFromAsset(context, "playlist.json")
        val gson = Gson()
        val listPersonType = object : TypeToken<List<Track>>() {}.type

        return gson.fromJson(jsonFileString, listPersonType)
    }
}