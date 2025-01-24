package com.walmart.assessment.ui.fragment

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.walmart.assessment.R
import com.walmart.assessment.data.Country
import com.walmart.assessment.data.json
import com.walmart.assessment.ui.viewmodel.CountryListViewModel
import com.walmart.assessment.ui.viewmodel.Failure
import com.walmart.assessment.ui.viewmodel.Loading
import com.walmart.assessment.ui.viewmodel.Success
import com.walmart.assessment.ui.viewmodel.UiState
import com.walmart.assessment.ui.viewmodel.factory.CountryListViewModelFactory
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalSerializationApi::class)
class CountryListFragmentTest {

    @MockK
    private lateinit var viewModel: CountryListViewModel

    @MockK
    private lateinit var viewModelFactory: CountryListViewModelFactory

    private val uiState = MutableStateFlow<UiState>(Loading)
    private var countriesList: List<Country>? = null

    private lateinit var scenario: FragmentScenario<CountryListFragment>

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        countriesList = InstrumentationRegistry
            .getInstrumentation()
            .context.assets.open("countries_list.json").let {
                json.decodeFromStream(it)
            }

        every { viewModelFactory.create(CountryListViewModel::class.java) } returns viewModel
        every { viewModel.uiState } returns uiState
        scenario = launchFragmentInContainer<CountryListFragment>(
            themeResId = R.style.Theme_Walmart
        ) {
            CountryListFragment(viewModelFactory)
        }
    }

    @Test
    fun fragment_launch() {

        onView(withId(R.id.loadingIndicator))
            .check(matches(isDisplayed()))

        onView(withId(R.id.countriesList))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.errorText))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun fragment_display_list() {
        scenario.moveToState(Lifecycle.State.STARTED)
        uiState.tryEmit(
            Success(countriesList!!)
        )

        onView(withId(R.id.loadingIndicator))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.countriesList))
            .check(matches(isDisplayed()))

        onView(withId(R.id.errorText))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun fragment_display_error() {
        var error = ""
        scenario.withFragment {
            error = requireActivity().getString(R.string.country_list_failure)
        }
        scenario.moveToState(Lifecycle.State.STARTED)
        uiState.tryEmit(
            Failure(RuntimeException("Testing error scenario"))
        )

        onView(withId(R.id.loadingIndicator))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.countriesList))
            .check(matches(not(isDisplayed())))

        onView(
            allOf(
                withId(R.id.errorText),
                withText(error)
            )
        ).check(matches(isDisplayed()))
    }
}