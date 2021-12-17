package sku.challenge.wiznphotos.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
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
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import sku.challenge.wiznphotos.repository.PhotoRepository
import sku.challenge.wiznphotos.test_utils.DummyData
import sku.challenge.wiznphotos.test_utils.mock
import sku.challenge.wiznphotos.vo.PhotoItem

@ExperimentalCoroutinesApi
class ItemViewModelTest {


    private val repository = mock<PhotoRepository>()

    private val items = DummyData.photoItems(1, 10)

    private val viewModel = ItemViewModel(repository)

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadCurrentItem() = runTest {
        val item1 = items[0]
        `when`(repository.getItem(1)).thenReturn(item1)
        `when`(repository.loadNextItem(item1)).thenReturn(PhotoItem.EMPTY_ITEM)
        `when`(repository.loadPreviousItem(item1)).thenReturn(PhotoItem.EMPTY_ITEM)

        viewModel.loadItem(1)
        yield()

        val result = viewModel.currentItem.first()

        assertThat(result, IsInstanceOf(ItemViewModel.PhotoItemResult.Success::class.java))

        val success = (result as ItemViewModel.PhotoItemResult.Success)

        assertThat(
            success.currentItem, `is`(
                equalTo(
                    item1
                )
            )
        )

        assertThat(success.nextItemAvailable, `is`(false))
        assertThat(success.prevItemAvailable, `is`(false))

        verify(repository).getItem(1)
    }

    @Test
    fun loadNextItem() = runTest {
        // TBH, I should be using a Fake here
        val itemWithId6 = items[5]
        val itemWithId7 = items[6]

        `when`(repository.getItem(6)).thenReturn(itemWithId6)
        `when`(repository.loadNextItem(itemWithId6)).thenReturn(itemWithId7)
        `when`(repository.loadPreviousItem(itemWithId6)).thenReturn(items[4])

        `when`(repository.getItem(7)).thenReturn(itemWithId7)
        `when`(repository.loadNextItem(itemWithId7)).thenReturn(items[7])
        `when`(repository.loadPreviousItem(itemWithId7)).thenReturn(itemWithId6)

        viewModel.loadItem(6)
        yield()

        viewModel.loadNextItem()
        // yield only makes one launch
        delay(20L)

        val result = viewModel.currentItem.first()

        assertThat(result, IsInstanceOf(ItemViewModel.PhotoItemResult.Success::class.java))

        val success = (result as ItemViewModel.PhotoItemResult.Success)

        assertThat(
            success.currentItem, `is`(
                equalTo(
                    itemWithId7
                )
            )
        )

        assertThat(success.nextItemAvailable, `is`(true))
        assertThat(success.prevItemAvailable, `is`(true))
    }

    @Test
    fun loadPreviousItem() = runTest {
        val itemWithId5 = items[4]
        val itemWithId6 = items[5]
        val itemWithId7 = items[6]

        `when`(repository.getItem(6)).thenReturn(itemWithId6)
        `when`(repository.loadNextItem(itemWithId6)).thenReturn(itemWithId7)
        `when`(repository.loadPreviousItem(itemWithId6)).thenReturn(itemWithId5)

        `when`(repository.getItem(5)).thenReturn(itemWithId5)
        `when`(repository.loadNextItem(itemWithId5)).thenReturn(itemWithId6)
        `when`(repository.loadPreviousItem(itemWithId5)).thenReturn(items[3])

        viewModel.loadItem(6)
        yield()

        viewModel.loadPreviousItem()
        // yield only makes one launch
        delay(20L)

        val result = viewModel.currentItem.first()

        assertThat(result, IsInstanceOf(ItemViewModel.PhotoItemResult.Success::class.java))

        val success = (result as ItemViewModel.PhotoItemResult.Success)

        assertThat(
            success.currentItem, `is`(
                equalTo(
                    itemWithId5
                )
            )
        )

        assertThat(success.nextItemAvailable, `is`(true))
        assertThat(success.prevItemAvailable, `is`(true))
    }

    @Test
    fun deleteItem() = runTest {
        val item1 = items[0]
        `when`(repository.getItem(1)).thenReturn(item1)
        `when`(repository.loadNextItem(item1)).thenReturn(PhotoItem.EMPTY_ITEM)
        `when`(repository.loadPreviousItem(item1)).thenReturn(PhotoItem.EMPTY_ITEM)

        viewModel.loadItem(1)
        yield()

        viewModel.deleteItem()
        yield()

        verify(repository).deleteItem(item1)
    }

    @Test
    @Ignore
    fun deleteItem_NoMoreItemsAreAvailable() {
        // todo:
        // will do it later
    }

    @Test
    fun starItem() = runTest {
        val item1 = items[0]
        `when`(repository.getItem(1)).thenReturn(item1)
        `when`(repository.loadNextItem(item1)).thenReturn(PhotoItem.EMPTY_ITEM)
        `when`(repository.loadPreviousItem(item1)).thenReturn(PhotoItem.EMPTY_ITEM)

        viewModel.loadItem(1)
        yield()

        viewModel.startItem()
        yield()

        verify(repository).bookmarkItem(item1)
    }

}