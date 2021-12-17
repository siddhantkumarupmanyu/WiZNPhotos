package sku.challenge.wiznphotos

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sku.challenge.wiznphotos.di.BaseUrl
import sku.challenge.wiznphotos.di.ConstantsModule
import sku.challenge.wiznphotos.test_utils.OkHttp3IdlingResource
import sku.challenge.wiznphotos.test_utils.RecyclerViewMatcher
import sku.challenge.wiznphotos.test_utils.enqueueResponse
import java.util.concurrent.TimeUnit
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
        mockWebServer.enqueueResponse("photos.json")
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun shouldHitRestApiOnlyOnce() = runBlocking {
        var activityScenario = ActivityScenario.launch(MainActivity::class.java)

        assertRequest("/photos")

        delay(300)
        onView(listMatcher().atPosition(1)).check(matches(hasDescendant(withText("reprehenderit est deserunt velit ipsam"))))

        activityScenario.close()

        activityScenario = ActivityScenario.launch(MainActivity::class.java)

        delay(100)
        onView(listMatcher().atPosition(1)).check(matches(hasDescendant(withText("reprehenderit est deserunt velit ipsam"))))

        activityScenario.close()

        assertThat(mockWebServer.requestCount, `is`(1))
    }

    @Test
    fun starAndDeleteItem() = runBlocking {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        assertRequest("/photos")

        delay(300)
        onView(listMatcher().atPosition(1)).check(matches(hasDescendant(withText("reprehenderit est deserunt velit ipsam"))))

        onView(listMatcher().atPosition(1)).perform(click())

        onView(ViewMatchers.withId(R.id.star_checkbox)).perform(click())

        onView(ViewMatchers.withId(R.id.star_checkbox)).check(matches(ViewMatchers.isChecked()))

        onView(ViewMatchers.withId(R.id.delete_checkbox)).perform(click())

        onView(ViewMatchers.withId(R.id.title_textview)).check(matches(not(withText("reprehenderit est deserunt velit ipsam"))))

        pressBack()

        onView(listMatcher().atPosition(1)).check(matches(not(hasDescendant(withText("reprehenderit est deserunt velit ipsam")))))

        activityScenario.close()
    }

    private fun listMatcher() = RecyclerViewMatcher(R.id.recycler_view_main_items)

    private fun setupRetrofitClient() {
        val resource = OkHttp3IdlingResource.create("okHttp", okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

    private fun assertRequest(path: String) {
        val request = mockWebServer.takeRequest(2, TimeUnit.SECONDS)
        assertThat(request.path, `is`(path))
    }

}