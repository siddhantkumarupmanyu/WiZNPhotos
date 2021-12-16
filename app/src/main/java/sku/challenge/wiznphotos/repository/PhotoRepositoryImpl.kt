package sku.challenge.wiznphotos.repository

import kotlinx.coroutines.flow.Flow
import sku.challenge.wiznphotos.api.ApiService
import sku.challenge.wiznphotos.db.PhotosDao
import sku.challenge.wiznphotos.vo.PhotoItem

// lack of better Name
class PhotoRepositoryImpl(
    private val api: ApiService,
    private val dao: PhotosDao
) : PhotoRepository {


    override suspend fun loadItems(): Flow<List<PhotoItem>> {
        if (dao.getCount() == 0) {
            val items = api.fetchPhotos()
            dao.insertPhotoItems(*items.toTypedArray())
        }
        return dao.getPhotos()
    }

    override suspend fun deleteItem(item: PhotoItem) {
        dao.deleteItem(item)
    }

    override suspend fun bookmarkItem(item: PhotoItem) {
        dao.updateItem(item.copy(isBookmarked = true))
    }
}