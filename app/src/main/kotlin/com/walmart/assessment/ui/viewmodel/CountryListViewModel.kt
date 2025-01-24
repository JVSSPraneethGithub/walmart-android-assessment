package com.walmart.assessment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walmart.assessment.data.Country
import com.walmart.assessment.data.CountryListApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UiState
object Loading : UiState
sealed class Completed : UiState
data class Success(val data: List<Country>) : Completed()
data class Failure(val error: Throwable) : Completed()

class CountryListViewModel(
    private val api: CountryListApi
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun fetchCountryList() {
        viewModelScope.launch {
            runCatching {
                api.fetchAllCountriesList()
            }.onSuccess { value ->
                _uiState.tryEmit(
                    if (value.isNotEmpty())
                        Success(value)
                    else Failure(IllegalStateException("Received Empty List"))
                )
            }.onFailure { error ->
                _uiState.tryEmit(
                    Failure(error)
                )
            }
        }
    }
}