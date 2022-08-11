package com.example.countries.model

data class Data(
    val code: String,
    val currencyCodes: List<String>,
    val name: String,
    val wikiDataId: String,
    val callingCode: String? = null,
    val capital: String? = null,
    val flagImageUri: String? = null,
    val numRegions: Int? = null,
    var isSaved: Boolean = false
)