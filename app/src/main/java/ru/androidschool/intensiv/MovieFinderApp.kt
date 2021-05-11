package ru.androidschool.intensiv

import android.app.Application
import androidx.room.Room
import ru.androidschool.intensiv.database.AppDatabase
import ru.androidschool.intensiv.network.MovieApiClient
import timber.log.Timber

class MovieFinderApp : Application() {

    private var database: AppDatabase? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        initDebugTools()
        repository = MovieApiClient
        database = Room.databaseBuilder(this, AppDatabase::class.java, "database").build()
    }
    private fun initDebugTools() {
        if (BuildConfig.DEBUG) {
            initTimber()
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    fun getRepository(): MovieApiClient? {
        return repository
    }

    fun getDatabase(): AppDatabase? {
        return database
    }

    companion object {
        var instance: MovieFinderApp? = null
            private set
        var repository: MovieApiClient? = null
            private set
    }
}
