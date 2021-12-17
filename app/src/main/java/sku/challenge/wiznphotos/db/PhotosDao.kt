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


    suspend fun getItem(id: Int): PhotoItem {
        return getItemImpl(id) ?: PhotoItem.EMPTY_ITEM
    }

    @Query("SELECT * FROM photoitem WHERE id = :id")
    protected abstract suspend fun getItemImpl(id: Int): PhotoItem?


    @Query("SELECT MAX(id) FROM photoitem")
    abstract suspend fun maxId(): Int

    @Query("SELECT MIN(id) FROM photoitem")
    abstract suspend fun minId(): Int

}