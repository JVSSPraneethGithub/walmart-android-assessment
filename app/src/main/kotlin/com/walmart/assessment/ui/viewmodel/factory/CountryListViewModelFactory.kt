package com.walmart.assessment.ui.viewmodel.factory

import androidx.lifecycle.ViewModelProvider
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.walmart.assessment.data.CountryListApi
import com.walmart.assessment.data.json
import com.walmart.assessment.ui.viewmodel.CountryListViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Suppress("UNCHECKED_CAST")
class CountryListViewModelFactory : ViewModelProvider.Factory {

    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountryListViewModel::class.java)) {
            return CountryListViewModel(
                Retrofit.Builder()
                    .baseUrl("https://gist.githubusercontent.com/")
                    .client(
                        OkHttpClient.Builder()
                            .addInterceptor(HttpLoggingInterceptor().apply {
                                setLevel(HttpLoggingInterceptor.Level.BODY)
                            })
                            .build()
                    ).addConverterFactory(
                        json.asConverterFactory(
                            "application/json; charset=UTF-8".toMediaType()
                        )
                    )
                    .build()
                    .create(CountryListApi::class.java)
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}