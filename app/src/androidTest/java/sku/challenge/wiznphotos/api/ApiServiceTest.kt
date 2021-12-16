package sku.challenge.wiznphotos.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sku.challenge.wiznphotos.test_utils.enqueueResponse
import sku.challenge.wiznphotos.vo.PhotoItem
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class ApiServiceTest {

    private val mockWebServer = MockWebServer()

    private lateinit var service: ApiService

    @Before
    fun setUp() {
        mockWebServer.start(8080)

        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun fetchPhotos() = runTest {
        mockWebServer.enqueueResponse("photos.json")

        val photos = service.fetchPhotos()

        val request = mockWebServer.takeRequest(2, TimeUnit.SECONDS)
        assertThat(request.path, `is`("/photos"))

        assertThat(photos.size, `is`(5000))

        assertPhotoItem(
            photos[0],
            1,
            "accusamus beatae ad facilis cum similique qui sunt",
            "https://via.placeholder.com/600/92c952"
        )

        assertPhotoItem(
            photos[6],
            7,
            "officia delectus consequatur vero aut veniam explicabo molestias",
            "https://via.placeholder.com/600/b0f7cc"
        )

    }

    private fun assertPhotoItem(item: PhotoItem, id: Int, title: String, url: String) {
        assertThat(item, `is`(equalTo(PhotoItem(id, title, url))))
    }

}