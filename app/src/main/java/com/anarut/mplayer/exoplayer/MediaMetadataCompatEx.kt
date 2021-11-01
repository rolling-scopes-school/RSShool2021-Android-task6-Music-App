package com.anarut.mplayer.exoplayer

import android.support.v4.media.MediaMetadataCompat
import com.anarut.mplayer.models.Track

fun MediaMetadataCompat.toTrack(): Track? {
    return description.let {
        Track(
            it.mediaId ?: "",
            it.title.toString(),
            it.subtitle.toString(),
            it.iconUri.toString(),
            it.mediaUri.toString(),
            0
        )
    }
}