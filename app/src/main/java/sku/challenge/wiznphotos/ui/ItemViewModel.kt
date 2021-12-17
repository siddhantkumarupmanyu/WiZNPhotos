package sku.challenge.wiznphotos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import sku.challenge.wiznphotos.repository.PhotoRepository
import sku.challenge.wiznphotos.vo.PhotoItem
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val repository: PhotoRepository
) : ViewModel() {

    private val _currentItem: MutableStateFlow<PhotoItemResult> =
        MutableStateFlow(PhotoItemResult.Loading)
    val currentItem: StateFlow<PhotoItemResult> = _currentItem

    private var nextItem = PhotoItem.EMPTY_ITEM
    private var prevItem = PhotoItem.EMPTY_ITEM

    fun loadItem(id: Int) {
        viewModelScope.launch {
            _currentItem.value = PhotoItemResult.Loading

            val currentItem = repository.getItem(id)
            nextItem = repository.loadNextItem(currentItem)
            prevItem = repository.loadPreviousItem(currentItem)
            _currentItem.value =
                PhotoItemResult.Success(currentItem, isItemEmpty(nextItem), isItemEmpty(prevItem))
        }
    }

    fun loadNextItem() {
        viewModelScope.launch {
            loadItem(nextItem.id)
        }
    }

    fun loadPreviousItem() {
        viewModelScope.launch {
            loadItem(prevItem.id)
        }
    }


    fun startItem() {
        viewModelScope.launch {
            val currentItem = (currentItem.value as PhotoItemResult.Success).currentItem
            repository.bookmarkItem(currentItem)
        }
    }

    fun deleteItem() {
        viewModelScope.launch {
            val currentItem = (currentItem.value as PhotoItemResult.Success).currentItem
            repository.deleteItem(currentItem)
        }
    }

    // }
    private fun isItemEmpty(item: PhotoItem) = item != PhotoItem.EMPTY_ITEM

    // currentItem should be a flow

    // nextItems and prevItems are just private


    sealed class PhotoItemResult {

        data class Success(
            val currentItem: PhotoItem,
            val nextItemAvailable: Boolean,
            val prevItemAvailable: Boolean
        ) : PhotoItemResult()

        object Loading : PhotoItemResult()

        object NoData : PhotoItemResult()
    }


}