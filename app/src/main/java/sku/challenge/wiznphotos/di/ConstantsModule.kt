package sku.challenge.wiznphotos.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

const val BASE_URL = "https://jsonplaceholder.typicode.com/"

@Module
@InstallIn(SingletonComponent::class)
object ConstantsModule {

    @Provides
    fun baseUrl(): BaseUrl {
        return BaseUrl(BASE_URL)
    }

}

data class BaseUrl(val baseUrl: String)