package com.example.countries.repository

import com.example.countries.model.Data
import com.example.countries.realm.CountryDao
import com.example.countries.realmDataModel.DataRM

class CountryRepository(private val countryDao: CountryDao) {

    fun updateItem(countryItemCode: String, isChecked: Boolean){
        countryDao.updateItem(countryItemCode, isChecked)
    }

    fun deleteItem(countryItemCode: String){
        countryDao.deleteItem(countryItemCode)
    }

    fun getAllSavedCountries() : ArrayList<Any> {
        return countryDao.getAllSavedCountries()
    }

    fun getAllCountries() : ArrayList<Any> {
        return countryDao.getAllCountries()
    }

    fun writeCountriesToRealm(data: List<Data>) {
        countryDao.writeCountriesToRealm(data)
    }

    fun getSingleCountry(countryItemCode: String) : Data {
        return countryDao.getSingleCountry(countryItemCode)
    }

}