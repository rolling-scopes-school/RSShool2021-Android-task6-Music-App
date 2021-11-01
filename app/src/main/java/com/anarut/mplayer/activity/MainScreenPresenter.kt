package com.anarut.mplayer.activity

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.anarut.mplayer.exoplayer.MusicServiceConnection
import com.anarut.mplayer.exoplayer.toTrack
import com.anarut.mplayer.models.Track
import com.anarut.mplayer.utils.Constants.LOGS
import com.anarut.mplayer.utils.Constants.MEDIA_ROOT_ID
import com.anarut.mplayer.utils.Constants.SELECTED_TRACK_INDEX
import com.anarut.mplayer.utils.isPlayEnabled
import com.anarut.mplayer.utils.isPrepared
import java.lang.Exception
import javax.inject.Inject

class MainScreenPresenter @Inject constructor(
    private val view: MainScreenInterface.View,
    private val musicServiceConnection: MusicServiceConnection
) : MainScreenInterface.Presenter {
    private var mediaItems = emptyList<Track>()
    private var logs = ""
    private var selectedTrackIndex = 0
    val currentPlayingTrack = musicServiceConnection.currentPlayingTrack
    private val playbackState = musicServiceConnection.playbackState

    init {
        musicServiceConnection.subscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    super.onChildrenLoaded(parentId, children)

                    val items = children.map {
                        Track(
                            it.mediaId ?: "",
                            it.description.title.toString(),
                            it.description.subtitle.toString(),
                            it.description.iconUri.toString(),
                            it.description.mediaUri.toString(),
                            0
                        )
                    }

                    mediaItems = items

                    try {
                        val selectedTrack = mediaItems[selectedTrackIndex]

                        view.displayTrack(selectedTrack)
                    } catch (e: Exception) {
                        print(e.localizedMessage)
                    }
                }
            })
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        bundle.putString(LOGS, logs)

        currentPlayingTrack.value?.let {
            val selectedTrackIndex = mediaItems.indexOf(it.toTrack())

            bundle.putInt(SELECTED_TRACK_INDEX, selectedTrackIndex)
        }
    }

    override fun onViewDestroyed() {
        musicServiceConnection.unsubscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }

    private fun playPrevious() {
        currentPlayingTrack.value?.toTrack()?.let {
            val currentTrackIndex = mediaItems.indexOf(it)
            val previousTrackIndex = currentTrackIndex - 1

            if (previousTrackIndex >= 0) {
                logEvent("previous track ${mediaItems[previousTrackIndex].title} was chosen...")
            }
        }

        musicServiceConnection.transportControls.skipToPrevious()
    }

    private fun playTrack(mediaItem: Track) {
        val isPrepared = playbackState.value?.isPrepared ?: false

        if (isPrepared && mediaItem.mediaId ==
            currentPlayingTrack.value?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
        ) {
            playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlayEnabled -> musicServiceConnection.transportControls.play()
                    else -> Unit
                }
            }
        } else {
            musicServiceConnection.transportControls.playFromMediaId(mediaItem.mediaId, null)
        }
    }

    private fun playNext() {
        currentPlayingTrack.value?.toTrack()?.let {
            val currentTrackIndex = mediaItems.indexOf(it)
            val nextTrackIndex = currentTrackIndex + 1

            if (nextTrackIndex <= mediaItems.size - 1) {
                logEvent("next track ${mediaItems[nextTrackIndex].title} was chosen...")
            }
        }

        musicServiceConnection.transportControls.skipToNext()
    }

    override fun onViewCreated(bundle: Bundle?) {
        bundle?.let {
            logs = it.getString(LOGS).toString()
            view.displayEvents(logs)

            selectedTrackIndex = it.getInt(SELECTED_TRACK_INDEX)
        }

        if (logs.isEmpty()) {
            logEvent("Start MediaSession...")
        }
    }

    override fun playButtonPressed() {
        currentPlayingTrack.value?.toTrack()?.let {
            playTrack(it)
            logEvent("track ${it.title} is playing...")
        }
    }

    override fun pauseButtonPressed() {
        pauseSound()

        currentPlayingTrack.value?.toTrack()?.let {
            logEvent("track ${it.title} was paused")
        }
    }

    override fun stopButtonPressed() {
        stopSound()

        currentPlayingTrack.value?.toTrack()?.let {
            logEvent("track ${it.title} was stopped")
        }
    }

    override fun prevButtonPressed() {
        playPrevious()
    }

    override fun nextButtonPressed() {
        playNext()
    }

    private fun pauseSound() {
        musicServiceConnection.transportControls.pause()
    }

    private fun stopSound() {
        musicServiceConnection.transportControls.pause()
    }

    private fun logEvent(description: String) {
        logs += description + "\n"

        view.displayEvents(logs)
    }
}