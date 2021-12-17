package sku.challenge.wiznphotos.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sku.challenge.wiznphotos.db.PhotosDao
import sku.challenge.wiznphotos.db.PhotosDb
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DbModule {

    @Singleton
    @Provides
    fun providesDb(app: Application): PhotosDb {
        return Room.databaseBuilder(app, PhotosDb::class.java, "tracks.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesTrackerDao(db: PhotosDb): PhotosDao {
        return db.photosDao()
    }

}