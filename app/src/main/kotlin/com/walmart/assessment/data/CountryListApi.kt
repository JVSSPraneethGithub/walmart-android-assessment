package com.walmart.assessment.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.http.GET

val json = Json { ignoreUnknownKeys = true }

interface CountryListApi {

    @GET("peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/countries.json")
    suspend fun fetchAllCountriesList(): List<Country>
}

@Serializable
data class Currency(
    @SerialName("code")
    val code: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("symbol")
    val symbol: String? = null
)

@Serializable
data class Language(
    @SerialName("code")
    val code: String? = null,
    @SerialName("name")
    val name: String? = null
)

@Serializable
data class Country(
    @SerialName("capital")
    val capital: String? = null,
    @SerialName("code")
    val code: String? = null,
    @SerialName("currency")
    val currency: Currency? = Currency(),
    @SerialName("flag")
    val flagImg: String? = null,
    @SerialName("language")
    val language: Language? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("region")
    val region: String? = null
)