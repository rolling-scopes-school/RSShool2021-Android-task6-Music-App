package com.anarut.mplayer.config

import android.app.Activity
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.anarut.mplayer.R
import com.anarut.mplayer.exoplayer.MusicServiceConnection
import com.anarut.mplayer.activity.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideGlideInstance(@ApplicationContext context: Context) = Glide
        .with(context)
        .setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_music)
                .error(R.drawable.ic_music)
                .diskCacheStrategy(
                    DiskCacheStrategy.DATA
                )
        )
}

@InstallIn(ActivityComponent::class)
@Module
object MainActivityModule {
    @Provides
    fun provideMusicServiceConnection(
        @ApplicationContext context: Context
    ) = MusicServiceConnection(context)

    @Provides
    fun bindActivity(activity: Activity): MainActivity {
        return activity as MainActivity
    }
}