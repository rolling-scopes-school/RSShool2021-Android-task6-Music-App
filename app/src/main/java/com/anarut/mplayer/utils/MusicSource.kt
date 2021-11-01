package com.anarut.mplayer.utils

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.core.net.toUri
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import javax.inject.Inject

class MusicSource @Inject constructor(
    private val mediaTrackResource: MediaTrackResource
) {
    var tracks = emptyList<MediaMetadataCompat>()

    fun loadTracks() {
        tracks = mediaTrackResource.loadTracks().map {
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, it.title)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, it.title)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, it.bitmapUri)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, it.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, it.trackUri)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, it.mediaId)
                .build()
        }
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSourceFactory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        tracks.forEach {
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(it.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri())

            concatenatingMediaSource.addMediaSource(mediaSource)
        }

        return concatenatingMediaSource
    }

    fun asMediaItems() = tracks.map {
        val desc = MediaDescriptionCompat.Builder()
            .setMediaUri(it.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri())
            .setTitle(it.description.title)
            .setSubtitle(it.description.subtitle)
            .setIconUri(it.description.iconUri)
            .setMediaId(it.description.mediaId)
            .build()

        MediaItem(desc, MediaItem.FLAG_PLAYABLE)
    }.toMutableList()
}

