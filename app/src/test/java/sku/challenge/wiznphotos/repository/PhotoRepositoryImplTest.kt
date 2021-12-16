package sku.challenge.wiznphotos.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.*
import sku.challenge.wiznphotos.api.ApiService
import sku.challenge.wiznphotos.db.PhotosDao
import sku.challenge.wiznphotos.test_utils.DummyData
import sku.challenge.wiznphotos.test_utils.mock
import sku.challenge.wiznphotos.vo.PhotoItem

@ExperimentalCoroutinesApi
class PhotoRepositoryImplTest {

    private val dao = mock<PhotosDao>()

    private val api = mock<ApiService>()

    private val repository = PhotoRepositoryImpl(dao, api)


    private val photoItems = DummyData.photoItems(1, 10)

    @Test
    fun shouldFetchFromNetworkAndSaveToDb_WhenDbIsEmpty() = runTest {
        `when`(dao.getCount()).thenReturn(0) // database is empty
        `when`(api.fetchPhotos()).thenReturn(photoItems)

        repository.loadItems()

        verify(dao).insertPhotoItems(*photoItems.toTypedArray())
        verify(dao).getPhotos()
        verify(api, times(1)).fetchPhotos()
    }

    @Test
    fun shouldFetchFromDatabase_WhenDbHaveAnyData() = runTest {
        `when`(dao.getCount()).thenReturn(10) // database is empty

        repository.loadItems()

        verifyNoInteractions(api)
        verify(dao).getPhotos()
    }

    @Test
    fun shouldBookmarkItem() = runTest {
        val item = PhotoItem(1, "title", "https://example.com/p/2")
        repository.bookmarkItem(item)

        verify(dao).updateItem(item.copy(isBookmarked = true))
    }

    @Test
    fun shouldDeleteItem() = runTest {
        val item = PhotoItem(1, "title", "https://example.com/p/2")
        repository.deleteItem(item)

        verify(dao).deleteItem(item)
    }

}