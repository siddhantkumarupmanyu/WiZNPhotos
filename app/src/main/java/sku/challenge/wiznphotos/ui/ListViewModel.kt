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

    private val _photosItems: MutableStateFlow<PhotosResult> =
        MutableStateFlow(PhotosResult.Success(listOf()))

    val photosItems: StateFlow<PhotosResult> = _photosItems

    init {
        viewModelScope.launch {
            _photosItems.value = PhotosResult.Loading
            repository.loadItems().collect {
                _photosItems.value = PhotosResult.Success(it)
            }
        }
    }

    // todo: these two functions shouldn't even be here fix them


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

    sealed class PhotosResult {
        class Success(val data: List<PhotoItem>) : PhotosResult()
        object Loading : PhotosResult()
    }

}