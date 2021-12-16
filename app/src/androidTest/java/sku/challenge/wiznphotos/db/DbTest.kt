package sku.challenge.wiznphotos.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before

abstract class DbTest {

    private lateinit var _db: PhotosDb

    val db: PhotosDb
        get() = _db

    @Before
    fun initDb() {
        _db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PhotosDb::class.java
        ).build()
    }

    @After
    fun closeDb() {
        _db.close()
    }

}