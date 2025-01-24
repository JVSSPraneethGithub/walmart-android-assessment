package com.walmart.assessment.ui.viewmodel

import com.walmart.assessment.data.Country
import com.walmart.assessment.data.CountryListApi
import com.walmart.assessment.data.json
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CountryListViewModelTest {

    @MockK
    private lateinit var listApi: CountryListApi
    private lateinit var countryListViewModel: CountryListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(Dispatchers.Unconfined)
        countryListViewModel = CountryListViewModel(listApi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun fetchCountries_shouldReturnCountryList() = runTest {
        val countriesList: List<Country>? = this::class.java.classLoader
            ?.getResourceAsStream("countries_list.json")?.let {
                json.decodeFromStream(it)
            }
        coEvery { listApi.fetchAllCountriesList() } returns countriesList!!

        assertEquals(Loading, countryListViewModel.uiState.value)

        countryListViewModel.fetchCountryList()
        advanceUntilIdle()

        val result = countryListViewModel.uiState.first()
        coVerify(exactly = 1) { listApi.fetchAllCountriesList() }
        assertTrue(result is Success)
        assertEquals(countriesList, (result as Success).data)
    }

    @Test
    fun fetchCountries_shouldHandleEmptyList() = runTest {
        val countriesList = emptyList<Country>()
        coEvery { listApi.fetchAllCountriesList() } returns countriesList

        assertEquals(Loading, countryListViewModel.uiState.value)

        countryListViewModel.fetchCountryList()
        advanceUntilIdle()

        val result = countryListViewModel.uiState.first()
        coVerify(exactly = 1) { listApi.fetchAllCountriesList() }
        assertTrue(result is Failure)
        assertNotNull((result as Failure).error.message)
        assertEquals("Received Empty List", result.error.message)
    }

    @Test
    fun fetchCountries_shouldHandleErrors() = runTest {
        coEvery { listApi.fetchAllCountriesList() } throws RuntimeException("Testing error scenario")

        assertEquals(Loading, countryListViewModel.uiState.value)

        countryListViewModel.fetchCountryList()
        advanceUntilIdle()

        val result = countryListViewModel.uiState.first()
        coVerify(exactly = 1) { listApi.fetchAllCountriesList() }
        assertTrue(result is Failure)
        assertNotNull((result as Failure).error.message)
        assertEquals("Testing error scenario", result.error.message)
    }
}
