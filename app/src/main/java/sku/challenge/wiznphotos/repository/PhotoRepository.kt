package sku.challenge.wiznphotos.repository

import kotlinx.coroutines.flow.Flow
import sku.challenge.wiznphotos.vo.PhotoItem

interface PhotoRepository {

    suspend fun loadItems(): Flow<List<PhotoItem>>

    suspend fun deleteItem(item: PhotoItem)

    suspend fun bookmarkItem(item: PhotoItem)

}