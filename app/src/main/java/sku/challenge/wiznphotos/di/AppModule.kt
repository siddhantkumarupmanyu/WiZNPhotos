package sku.challenge.wiznphotos.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sku.challenge.wiznphotos.api.ApiService
import sku.challenge.wiznphotos.db.PhotosDao
import sku.challenge.wiznphotos.repository.PhotoRepository
import sku.challenge.wiznphotos.repository.PhotoRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideRepository(
        service: ApiService,
        dao: PhotosDao
    ): PhotoRepository {
        return PhotoRepositoryImpl(service, dao)
    }


}