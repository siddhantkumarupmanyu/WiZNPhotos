package sku.challenge.wiznphotos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sku.challenge.wiznphotos.repository.PhotoRepository
import sku.challenge.wiznphotos.vo.PhotoItem
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: PhotoRepository
) : ViewModel() {
    // val photoItems: StateFlow = repository.

    init {
        viewModelScope.launch {
            repository.loadItems()
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

}