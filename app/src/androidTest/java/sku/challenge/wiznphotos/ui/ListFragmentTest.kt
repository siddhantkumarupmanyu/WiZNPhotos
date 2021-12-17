package sku.challenge.wiznphotos.ui

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import sku.challenge.wiznphotos.R
import sku.challenge.wiznphotos.di.AppModule
import sku.challenge.wiznphotos.repository.PhotoRepository
import sku.challenge.wiznphotos.test_utils.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@UninstallModules(AppModule::class)
@HiltAndroidTest
class ListFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @JvmField
    @Rule
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule()

    @BindValue
    val repository: PhotoRepository = mock()

    private val navController = mock<NavController>()

    private val items = DummyData.photoItems(1, 10)

    @Before
    fun setUp() = runTest {
        // Populate @Inject fields in test class
        hiltRule.inject()
    }

    @Test
    fun progressBarVisible_WhenLoading(): Unit = runBlocking {
        // which class should be responsible for loading status viewModel Or only View
        // for this simple case let's give this responsibility to view only

        `when`(repository.loadItems()).thenReturn(flow {
            delay(100L)
            emit(items)
        })

        launchFragmentInHiltContainer<ListFragment> {
            dataBindingIdlingResourceRule.monitorFragment(this)
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.progress_indicator)).check(matches(isDisplayed()))

        delay(200L)

        onView(withId(R.id.progress_indicator)).check(matches(not(isDisplayed())))

        onView(listMatcher().atPosition(1)).check(matches(hasDescendant(withText("title: 2"))))
    }

    @Test
    fun shouldNavigateToItemFragmentOnClick(): Unit = runBlocking {
        `when`(repository.loadItems()).thenReturn(flow {
            emit(items)
        })

        launchFragmentInHiltContainer<ListFragment> {
            dataBindingIdlingResourceRule.monitorFragment(this)
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(listMatcher().atPosition(1)).check(matches(hasDescendant(withText("title: 2"))))

        onView(listMatcher().atPosition(0)).perform(click())

        verify(navController).navigate(ListFragmentDirections.actionToItemFragment())
    }

    private fun listMatcher() = RecyclerViewMatcher(R.id.list_view)
}
