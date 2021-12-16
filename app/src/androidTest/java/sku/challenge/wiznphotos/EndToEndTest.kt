package sku.challenge.wiznphotos

import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import sku.challenge.wiznphotos.di.BaseUrl
import sku.challenge.wiznphotos.di.ConstantsModule
import sku.challenge.wiznphotos.test_utils.OkHttp3IdlingResource
import sku.challenge.wiznphotos.test_utils.RecyclerViewMatcher
import sku.challenge.wiznphotos.test_utils.enqueueResponse
import javax.inject.Inject


@LargeTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(ConstantsModule::class)
@HiltAndroidTest
class EndToEndTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val mockWebServer = MockWebServer()

    @BindValue
    val baseUrl: BaseUrl = BaseUrl("http://127.0.0.1:8080")

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Before
    fun startServer() {
        hiltRule.inject()

        setupRetrofitClient()

        mockWebServer.start(8080)
        mockWebServer.enqueueResponse("page1.json")
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun shouldHitRestApiOnlyOnce() {

    }

    @Test
    @Ignore
    fun starAndDeleteItem() {

    }

    private fun listMatcher() = RecyclerViewMatcher(R.id.list_view)

    private fun setupRetrofitClient() {
        val resource = OkHttp3IdlingResource.create("okHttp", okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

}