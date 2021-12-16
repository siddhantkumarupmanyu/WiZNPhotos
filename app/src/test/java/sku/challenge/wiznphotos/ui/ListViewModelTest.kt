package sku.challenge.wiznphotos.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.yield
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import sku.challenge.wiznphotos.repository.PhotoRepository
import sku.challenge.wiznphotos.test_utils.mock
import sku.challenge.wiznphotos.vo.PhotoItem

@ExperimentalCoroutinesApi
class ListViewModelTest {

    private val repository = mock<PhotoRepository>()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldLoadItemsOnInitialization() = runTest {
        val viewModel = ListViewModel(repository)
        yield()

        verify(repository).loadItems()
    }

    @Test
    fun shouldBookmarkItem() = runTest {
        val viewModel = ListViewModel(repository)
        yield()

        val item = PhotoItem(1, "title", "https://example.com/p/1")

        viewModel.bookmarkItem(item)
        yield()

        verify(repository).bookmarkItem(item)
    }

    @Test
    fun shouldDeleteItem() = runTest {
        val viewModel = ListViewModel(repository)
        yield()

        val item = PhotoItem(1, "title", "https://example.com/p/1")

        viewModel.deleteItem(item)
        yield()

        verify(repository).deleteItem(item)
    }

}