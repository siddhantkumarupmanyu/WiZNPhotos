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
        // TODO: fix this
        dao.updateItem(item.copy(isBookmarked = true))
    }

    override suspend fun getItem(id: Int): PhotoItem {
        return dao.getItem(id)
    }


    // todo: should refactor these two functions and make them clean

    override suspend fun loadNextItem(item: PhotoItem): PhotoItem {
        // todo: fail fast id is less than min id or max than min id

        val minId = dao.minId()
        val maxId = dao.maxId()

        var nextItem = PhotoItem.EMPTY_ITEM

        for (i in (item.id + 1)..maxId) {
            val currentItem = dao.getItem(i)
            if (currentItem != PhotoItem.EMPTY_ITEM) {
                nextItem = currentItem
                break
            }
        }
        // we have reached the end and could not find any item return empty Item
        return nextItem
    }

    override suspend fun loadPreviousItem(item: PhotoItem): PhotoItem {
        // todo: fail fast id is less than min id or max than min id

        val minId = dao.minId()
        val maxId = dao.maxId()

        var nextItem = PhotoItem.EMPTY_ITEM

        for (i in (item.id - 1) downTo minId) {
            val currentItem = dao.getItem(i)
            if (currentItem != PhotoItem.EMPTY_ITEM) {
                nextItem = currentItem
                break
            }
        }
        // we have reached the end and could not find any item return empty Item
        return nextItem
    }
}