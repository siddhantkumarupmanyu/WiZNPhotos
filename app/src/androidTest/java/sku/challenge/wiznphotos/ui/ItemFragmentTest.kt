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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sku.challenge.wiznphotos.R
import sku.challenge.wiznphotos.di.AppModule
import sku.challenge.wiznphotos.repository.PhotoRepository
import sku.challenge.wiznphotos.test_utils.DataBindingIdlingResourceRule
import sku.challenge.wiznphotos.test_utils.DummyData
import sku.challenge.wiznphotos.test_utils.launchFragmentInHiltContainer
import sku.challenge.wiznphotos.test_utils.mock
import sku.challenge.wiznphotos.vo.PhotoItem

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@UninstallModules(AppModule::class)
@HiltAndroidTest
class ItemFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @JvmField
    @Rule
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule()

    @BindValue
    val repository: PhotoRepository = FakeRepository()

    private val navController = mock<NavController>()

    @Before
    fun setUp() = runTest {
        // Populate @Inject fields in test class
        hiltRule.inject()

    }

    @Test
    fun shouldLoadRequiredItem() {
        val args = ItemFragmentArgs(4).toBundle()
        launchFragmentInHiltContainer<ItemFragment>(fragmentArgs = args, action = {
            dataBindingIdlingResourceRule.monitorFragment(this)
            Navigation.setViewNavController(requireView(), navController)
        })

        onView(withId(R.id.title_textview)).check(matches(withText("title: 4")))
    }

    @Test
    fun showNextItem_WhenClickedOnRightArrow(): Unit = runBlocking {
        val args = ItemFragmentArgs(4).toBundle()
        launchFragmentInHiltContainer<ItemFragment>(fragmentArgs = args, action = {
            dataBindingIdlingResourceRule.monitorFragment(this)
            Navigation.setViewNavController(requireView(), navController)
        })

        onView(withId(R.id.next_arrow)).perform(click())

        delay(20)
        onView(withId(R.id.title_textview)).check(matches(withText("title: 5")))
    }

    @Test
    fun showPreviousItem_WhenClickedOnLeftArrow(): Unit = runBlocking {
        val args = ItemFragmentArgs(4).toBundle()
        launchFragmentInHiltContainer<ItemFragment>(fragmentArgs = args, action = {
            dataBindingIdlingResourceRule.monitorFragment(this)
            Navigation.setViewNavController(requireView(), navController)
        })

        onView(withId(R.id.previous_arrow)).perform(click())

        delay(20)
        onView(withId(R.id.title_textview)).check(matches(withText("title: 3")))
    }

    @Test
    fun shouldNotShowPreviousArrowInCaseOfFirstItem() {
        val args = ItemFragmentArgs(1).toBundle()
        launchFragmentInHiltContainer<ItemFragment>(fragmentArgs = args, action = {
            dataBindingIdlingResourceRule.monitorFragment(this)
            Navigation.setViewNavController(requireView(), navController)
        })

        onView(withId(R.id.previous_arrow)).check(matches(not(isDisplayed())))
    }

    @Test
    fun shouldNotShowNextArrowInCaseOfLastItem() {
        val args = ItemFragmentArgs(10).toBundle()
        launchFragmentInHiltContainer<ItemFragment>(fragmentArgs = args, action = {
            dataBindingIdlingResourceRule.monitorFragment(this)
            Navigation.setViewNavController(requireView(), navController)
        })

        onView(withId(R.id.next_arrow)).check(matches(not(isDisplayed())))
    }

    @Test
    fun showNextItemOnDeletion(): Unit = runBlocking {
        val args = ItemFragmentArgs(5).toBundle()
        launchFragmentInHiltContainer<ItemFragment>(fragmentArgs = args, action = {
            dataBindingIdlingResourceRule.monitorFragment(this)
            Navigation.setViewNavController(requireView(), navController)
        })

        onView(withId(R.id.delete_checkbox)).perform(click())

        onView(withId(R.id.title_textview)).check(matches(withText("title: 6")))
    }

    @Test
    fun shouldStarItem() {
        val args = ItemFragmentArgs(5).toBundle()
        launchFragmentInHiltContainer<ItemFragment>(fragmentArgs = args, action = {
            dataBindingIdlingResourceRule.monitorFragment(this)
            Navigation.setViewNavController(requireView(), navController)
        })

        onView(withId(R.id.star_checkbox)).perform(click())

        onView(withId(R.id.star_checkbox)).check(matches(isChecked()))
    }

    @Test
    @Ignore
    fun shouldNavigateBackIfThereAreNoMoreDataAfterDeletion() {
        // todo:
        // will do it later
    }

    class FakeRepository : PhotoRepository {

        var items = DummyData.photoItems(1, 10)

        override suspend fun loadItems(): Flow<List<PhotoItem>> {
            // return flowOf(items)
            TODO()
        }

        override suspend fun deleteItem(item: PhotoItem) {
            val list = mutableListOf<PhotoItem>()
            list.addAll(items)
            list.removeAt(item.id - 1)
            items = list
        }

        override suspend fun toggleBookmark(item: PhotoItem) {
            val list = mutableListOf<PhotoItem>()
            list.addAll(items)
            val index = list.indexOf(item)
            list.removeAt(index)
            list.add(index, item.copy(isBookmarked = !item.isBookmarked))
            items = list
        }

        override suspend fun getItem(id: Int): PhotoItem {
            return items.single {
                it.id == id
            }
        }

        override suspend fun loadNextItem(item: PhotoItem): PhotoItem {
            return items.getOrElse(item.id) { PhotoItem.EMPTY_ITEM }
        }

        override suspend fun loadPreviousItem(item: PhotoItem): PhotoItem {
            return items.getOrElse(item.id - 2) { PhotoItem.EMPTY_ITEM }
        }

    }

}