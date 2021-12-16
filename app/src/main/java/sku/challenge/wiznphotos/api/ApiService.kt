package sku.challenge.wiznphotos.api

import retrofit2.http.GET
import sku.challenge.wiznphotos.vo.PhotoItem

interface ApiService {

    @GET("/photos")
    suspend fun fetchPhotos(): List<PhotoItem>

}