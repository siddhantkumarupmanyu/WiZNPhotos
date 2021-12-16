package sku.challenge.wiznphotos.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import sku.challenge.wiznphotos.vo.PhotoItem


@Dao
abstract class PhotosDao {

    @Insert
    abstract fun insertPhotoItems(vararg photos: PhotoItem)

    @Query("SELECT * FROM photoitem")
    abstract fun getPhotos(): Flow<List<PhotoItem>>

    @Delete
    abstract fun deletePhotoItem(item: PhotoItem)

    @Update
    abstract fun updateItem(updatedItem: PhotoItem)


}