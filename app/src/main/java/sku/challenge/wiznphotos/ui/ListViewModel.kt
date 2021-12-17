package sku.challenge.wiznphotos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import sku.challenge.wiznphotos.repository.PhotoRepository
import sku.challenge.wiznphotos.vo.PhotoItem
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: PhotoRepository
) : ViewModel() {

    private val _photoItems: MutableStateFlow<PhotoResult> =
        MutableStateFlow(PhotoResult.Success(listOf()))

    val photoItems: StateFlow<PhotoResult> = _photoItems

    init {
        viewModelScope.launch {
            _photoItems.value = PhotoResult.Loading
            repository.loadItems().collect {
                _photoItems.value = PhotoResult.Success(it)
            }
        }
    }

    fun bookmarkItem(item: PhotoItem) {
        viewModelScope.launch {
            repository.bookmarkItem(item)
        }
    }

    fun deleteItem(item: PhotoItem) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    sealed class PhotoResult {
        class Success(val data: List<PhotoItem>) : PhotoResult()
        object Loading : PhotoResult()
    }

}