package sku.challenge.wiznphotos.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import sku.challenge.wiznphotos.vo.PhotoItem


@Dao
abstract class PhotosDao {

    @Insert
    abstract suspend fun insertPhotoItems(vararg photos: PhotoItem)

    @Query("SELECT * FROM photoitem")
    abstract fun getPhotos(): Flow<List<PhotoItem>>

    @Delete
    abstract suspend fun deleteItem(item: PhotoItem)

    @Update
    abstract suspend fun updateItem(updatedItem: PhotoItem)


    @Query("SELECT COUNT(*) FROM photoitem")
    abstract suspend fun getCount(): Int


}