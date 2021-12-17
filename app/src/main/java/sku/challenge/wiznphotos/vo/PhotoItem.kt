package sku.challenge.wiznphotos.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhotoItem(
    @PrimaryKey
    val id: Int,
    val title: String,
    val url: String,
    val isBookmarked: Boolean = false
) {

    companion object {

        var EMPTY_ITEM = PhotoItem(-1, "", "", false)

    }
}