package sku.challenge.wiznphotos.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual.equalTo
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

    private val repository = PhotoRepositoryImpl(api, dao)


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

    @Test
    fun getItem() = runTest {
        repository.getItem(1)

        verify(dao).getItem(1)
    }

    @Test
    fun loadNextItem() = runTest {
        val item = PhotoItem(1, "title", "https://example.com/p/1")
        setUpDaoFake(item)

        assertThat(repository.loadNextItem(item), `is`(equalTo(item.copy(id = 2))))

        assertThat(repository.loadNextItem(item.copy(id = 2)), `is`(equalTo(item.copy(id = 5))))

        assertThat(repository.loadNextItem(item.copy(6)), `is`(PhotoItem.EMPTY_ITEM))

        verify(dao, never()).getItem(7)
    }

    @Test
    fun previousItem() = runTest {
        val item = PhotoItem(1, "title", "https://example.com/p/1")
        setUpDaoFake(item)

        assertThat(repository.loadPreviousItem(item.copy(6)), `is`(equalTo(item.copy(id = 5))))

        assertThat(repository.loadPreviousItem(item.copy(id = 5)), `is`(equalTo(item.copy(id = 2))))

        assertThat(repository.loadPreviousItem(item.copy(2)), `is`(equalTo(item.copy(1))))
        assertThat(repository.loadPreviousItem(item.copy(1)), `is`(PhotoItem.EMPTY_ITEM))

        verify(dao).getItem(1)
        verify(dao, never()).getItem(0)
    }

    private suspend fun setUpDaoFake(item: PhotoItem) {
        `when`(dao.maxId()).thenReturn(6)
        `when`(dao.minId()).thenReturn(1)
        `when`(dao.getItem(0)).thenReturn(
            PhotoItem.EMPTY_ITEM
        )
        `when`(dao.getItem(1)).thenReturn(
            item.copy(id = 1)
        )
        `when`(dao.getItem(2)).thenReturn(
            item.copy(id = 2)
        )
        `when`(dao.getItem(3)).thenReturn( // third item is deleted
            PhotoItem.EMPTY_ITEM
        )
        `when`(dao.getItem(4)).thenReturn( // fourth item is deleted
            PhotoItem.EMPTY_ITEM
        )
        `when`(dao.getItem(5)).thenReturn(
            item.copy(id = 5)
        )
        `when`(dao.getItem(6)).thenReturn(
            item.copy(id = 6)
        )
        `when`(dao.getItem(7)).thenReturn(
            PhotoItem.EMPTY_ITEM
        )
    }

}