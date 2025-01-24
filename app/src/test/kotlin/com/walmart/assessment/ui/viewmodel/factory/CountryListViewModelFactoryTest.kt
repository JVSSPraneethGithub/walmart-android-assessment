package com.walmart.assessment.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import com.walmart.assessment.ui.viewmodel.CountryListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CountryListViewModelFactoryTest {

    private class UnknownViewModel : ViewModel()

    private lateinit var factory: CountryListViewModelFactory

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        factory = CountryListViewModelFactory()
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun valid_viewmodel() = runTest {
        val result = factory.create(CountryListViewModel::class.java)
        assertTrue(result is CountryListViewModel)
    }

    @Test
    fun invalid_viewmodel() = runTest {
        runCatching {
            factory.create(UnknownViewModel::class.java)
        }.onFailure { error ->
            assertTrue(error is IllegalArgumentException)
            assertEquals("Unknown ViewModel class", error.message)
        }
    }
}