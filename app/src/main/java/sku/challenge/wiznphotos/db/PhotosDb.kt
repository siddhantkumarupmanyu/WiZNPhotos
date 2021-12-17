package sku.challenge.wiznphotos.db

import androidx.room.Database
import androidx.room.RoomDatabase
import sku.challenge.wiznphotos.vo.PhotoItem


@Database(
    entities = [PhotoItem::class],
    version = 1,
    exportSchema = false
)
abstract class PhotosDb : RoomDatabase() {
    abstract fun photosDao(): PhotosDao
}