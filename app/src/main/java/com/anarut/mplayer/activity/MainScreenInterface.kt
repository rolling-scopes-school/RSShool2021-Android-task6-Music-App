package com.anarut.mplayer.activity

import android.os.Bundle
import com.anarut.mplayer.models.Track

interface MainScreenInterface {
    interface Presenter {
        fun onViewCreated(bundle: Bundle?)
        fun onSaveInstanceState(bundle: Bundle)
        fun onViewDestroyed()
        fun playButtonPressed()
        fun pauseButtonPressed()
        fun stopButtonPressed()
        fun prevButtonPressed()
        fun nextButtonPressed()
    }

    interface View {
        fun displayTrack(track: Track)
        fun displayEvents(events: String)
    }
}