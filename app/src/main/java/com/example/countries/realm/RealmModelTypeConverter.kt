package com.example.countries.realm

import com.example.countries.model.Data
import com.example.countries.realmDataModel.DataRM
import io.realm.RealmList

object RealmModelTypeConverter {

    fun getCountryDataModel(countryRealmModel: DataRM) : Data
    {
        val theCurrencyCodesList = arrayListOf<String>()
        countryRealmModel.currencyCodes.forEach { theCurrencyCodesList.add(it) }

        return Data(
            countryRealmModel.code,
            theCurrencyCodesList,
            countryRealmModel.name,
            countryRealmModel.wikiDataId,
            countryRealmModel.callingCode,
            countryRealmModel.capital,
            countryRealmModel.flagImageUri,
            countryRealmModel.numRegions,
            countryRealmModel.isSaved
        )
    }

    fun getCountryRealmModel(countryDataModel: Data) : DataRM
    {
        val theCurrencyCodesList = RealmList<String>()
        countryDataModel.currencyCodes.forEach { theCurrencyCodesList.add(it) }

        return DataRM(
            countryDataModel.code,
            theCurrencyCodesList,
            countryDataModel.name,
            countryDataModel.wikiDataId,
            countryDataModel.callingCode,
            countryDataModel.capital,
            countryDataModel.flagImageUri,
            countryDataModel.numRegions,
            countryDataModel.isSaved
        )
    }

}