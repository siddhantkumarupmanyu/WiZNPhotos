package sku.challenge.wiznphotos.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import sku.challenge.wiznphotos.vo.PhotoItem

// tests should never extend, every test should be decoupled from other
// ignoring this one for now

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class PhotosDaoTest : DbTest() {

    private val photosDao
        get() = db.photosDao()

    @Test
    fun getItem() = runTest {
        val item1 = PhotoItem(1, "title1", "https://exmample.com/photos/1")

        photosDao.insertPhotoItems(item1)

        val photo = photosDao.getItem(1)

        assertThat(photo, `is`(equalTo(item1)))

        assertThat(photosDao.getItem(2), `is`(PhotoItem.EMPTY_ITEM))
    }

    @Test
    fun minAndMaxId() = runTest {
        val item1 = PhotoItem(1, "title1", "https://exmample.com/photos/1")
        val item2 = PhotoItem(10, "title2", "https://exmample.com/photos/2")

        photosDao.insertPhotoItems(item1, item2)

        assertThat(photosDao.maxId(), `is`(10))
        assertThat(photosDao.minId(), `is`(1))
    }

    @Test
    fun insertPhotoItems() = runTest {
        val item1 = PhotoItem(1, "title1", "https://exmample.com/photos/1")
        val item2 = PhotoItem(2, "title2", "https://exmample.com/photos/2")

        photosDao.insertPhotoItems(item1, item2)

        val photos = photosDao.getPhotos().first()

        assertThat(photos[0], `is`(equalTo(item1)))
        assertThat(photos[1], `is`(equalTo(item2)))

        assertThat(photosDao.getCount(), `is`(2))
    }

    @Test
    fun deletePhotoItem() = runTest {
        val item1 = PhotoItem(1, "title1", "https://exmample.com/photos/1")
        val item2 = PhotoItem(2, "title2", "https://exmample.com/photos/2")

        photosDao.insertPhotoItems(item1, item2)

        var photos = photosDao.getPhotos().first()

        assertThat(photos.size, `is`(2))
        assertThat(photos[0], `is`(equalTo(item1)))

        photosDao.deleteItem(item1)

        photos = photosDao.getPhotos().first()

        assertThat(photos.size, `is`(1))
        assertThat(photos[0], `is`(equalTo(item2)))

        assertThat(photosDao.getCount(), `is`(1))
    }

    @Test
    fun updatePhotoItem() = runTest {
        val item1 = PhotoItem(1, "title1", "https://exmample.com/photos/1")
        val item2 = PhotoItem(2, "title2", "https://exmample.com/photos/2")

        photosDao.insertPhotoItems(item1, item2)

        var photos = photosDao.getPhotos().first()

        assertThat(photos[0], `is`(equalTo(item1)))
        assertThat(photos[1], `is`(equalTo(item2)))

        photosDao.updateItem(item1.copy(isBookmarked = true))

        photos = photosDao.getPhotos().first()

        assertThat(photos[0], `is`(equalTo(item1.copy(isBookmarked = true))))
        assertThat(photos[1], `is`(equalTo(item2)))

        assertThat(photosDao.getCount(), `is`(2))
    }

}