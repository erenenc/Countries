package com.example.countries.realmDataModel

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DataRM (
    @PrimaryKey
    var code: String = "",
    var currencyCodes: RealmList<String> = RealmList(),
    var name: String = "",
    var wikiDataId: String = "",
    var callingCode: String? = null,
    var capital: String? = null,
    var flagImageUri: String? = null,
    var numRegions: Int? = null,
    var isSaved: Boolean = false
): RealmObject()