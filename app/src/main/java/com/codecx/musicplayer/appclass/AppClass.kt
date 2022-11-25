package com.codecx.musicplayer.appclass

import android.app.Application
import com.codecx.musicplayer.repo.FavRepository
import com.codecx.musicplayer.repo.PlaylistPageRepository
import com.codecx.musicplayer.livedataviewmodels.FavViewModel
import com.codecx.musicplayer.livedataviewmodels.PlaylistPageViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AppClass)
            modules(
                listOf(
                    favModule, playListPageModule
                )
            )
        }
    }

    val favModule = module {
        single { FavRepository() }
        viewModel { FavViewModel(get()) }
    }

    val playListPageModule = module {
        single { PlaylistPageRepository() }
        viewModel { PlaylistPageViewModel(get()) }
    }

}