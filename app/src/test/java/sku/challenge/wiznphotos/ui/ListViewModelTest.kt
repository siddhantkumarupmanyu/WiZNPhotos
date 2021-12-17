package sku.challenge.wiznphotos.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.yield
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual.equalTo
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import sku.challenge.wiznphotos.repository.PhotoRepository
import sku.challenge.wiznphotos.test_utils.DummyData
import sku.challenge.wiznphotos.test_utils.mock
import sku.challenge.wiznphotos.vo.PhotoItem

@ExperimentalCoroutinesApi
class ListViewModelTest {

    private val repository = mock<PhotoRepository>()

    private val items = DummyData.photoItems(1, 10)

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
        `when`(repository.loadItems()).thenReturn(flow {
            delay(20L)
            emit(items)
        })

        val viewModel = ListViewModel(repository)
        yield()

        assertThat(
            viewModel.photoItems.first(),
            IsInstanceOf(ListViewModel.PhotoResult.Loading::class.java)
        )

        delay(50L)

        assertThat(
            viewModel.photoItems.first(),
            IsInstanceOf(ListViewModel.PhotoResult.Success::class.java)
        )

        assertThat(
            (viewModel.photoItems.first() as ListViewModel.PhotoResult.Success).data,
            `is`(equalTo(items))
        )

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