package com.anarut.mplayer.activity

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.anarut.mplayer.databinding.ActivityMainBinding
import com.anarut.mplayer.exoplayer.toTrack
import com.anarut.mplayer.models.Track
import com.anarut.mplayer.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainScreenInterface.View {
    @Inject
    lateinit var presenter: MainScreenPresenter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.descriptionTextView.movementMethod = ScrollingMovementMethod()

        setContentView(binding.root)

        setUpActionButtons()
        setUpObservers()

        presenter.onViewCreated(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        presenter.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.onViewDestroyed()
    }

    private fun setUpObservers() {
        presenter.currentPlayingTrack.observe(this) {
            if (it == null) return@observe

            val track = it.toTrack()

            track?.let { track -> displayTrack(track) }
        }
    }

    private fun setUpActionButtons() {
        prevButtonSetUp()
        playButtonSetUp()
        pauseButtonSetUp()
        stopButtonSetUp()
        nextButtonSetUp()
    }

    private fun prevButtonSetUp() {
        binding.prevButton.setOnClickListener {
            presenter.prevButtonPressed()
        }
    }

    private fun playButtonSetUp() {
        binding.playButton.setOnClickListener {
            presenter.playButtonPressed()
        }
    }

    private fun pauseButtonSetUp() {
        binding.pauseButton.setOnClickListener {
            presenter.pauseButtonPressed()
        }
    }

    private fun stopButtonSetUp() {
        binding.stopButton.setOnClickListener {
            presenter.stopButtonPressed()
        }
    }

    private fun nextButtonSetUp() {
        binding.nextButton.setOnClickListener {
            presenter.nextButtonPressed()
        }
    }

    override fun displayTrack(track: Track) {
        binding.titleTextView.text = "${track.artist} - ${track.title}"
        binding.iconImageView.let { Glide.with(this).load(track.bitmapUri).into(it) }
    }

    override fun displayEvents(events: String) {
        binding.descriptionTextView.text = events
    }
}