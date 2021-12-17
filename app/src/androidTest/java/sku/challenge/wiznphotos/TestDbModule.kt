package sku.challenge.wiznphotos

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import sku.challenge.wiznphotos.db.PhotosDao
import sku.challenge.wiznphotos.db.PhotosDb
import sku.challenge.wiznphotos.di.DbModule
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DbModule::class]
)
object TestDbModule {

    @Singleton
    @Provides
    fun providesDb(app: Application): PhotosDb {
        return Room.inMemoryDatabaseBuilder(
            app,
            PhotosDb::class.java
        ).build()
    }

    @Singleton
    @Provides
    fun providesTrackerDb(db: PhotosDb): PhotosDao {
        return db.photosDao()
    }

}