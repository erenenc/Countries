package com.example.countries.realm

import android.util.Log
import android.view.View
import com.example.countries.RetrofitInstance
import com.example.countries.fragments.TAG
import com.example.countries.model.Country
import com.example.countries.model.Data
import com.example.countries.realmDataModel.DataRM
import io.realm.kotlin.where
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

object CountryDao {

    val theRealm by lazy { RealmInstanceHelper.getInstance() }

    // deletes country from realm
    fun deleteItem(deletedItemCode: String) {

        val deletedItem = getSingleCountry(deletedItemCode)

        theRealm.executeTransaction { theRealm ->
            theRealm.where<DataRM>().equalTo("code", deletedItem.code).findFirst().let {
                it?.deleteFromRealm()
            }
        }
    }

    // controls the "saved" status of country
    fun updateItem(savedItemCode: String, isChecked: Boolean) {

        val savedItem = getSingleCountry(savedItemCode)

        theRealm.executeTransaction { theRealm ->
            val country = theRealm.where<DataRM>()
                .equalTo("code", savedItem.code)
                .findFirst()
            country?.isSaved = isChecked

        }

    }


    // get all saved countries from realm
    fun getAllSavedCountries() : ArrayList<Any> {

        val savedCountryList = arrayListOf<Any>()

        val savedCountryRealmList = theRealm.where<DataRM>().equalTo("isSaved", true).findAll()
        savedCountryRealmList.forEach {
            savedCountryList.add(RealmModelTypeConverter.getCountryDataModel(it))
        }
        savedCountryList.add(1)

        return savedCountryList

    }

    // get all countries from realm
    fun getAllCountries() : ArrayList<Any> {

        val countryListDMFromRealm = arrayListOf<Any>()
        val countryListRMFromRealm = theRealm.where<DataRM>().findAll()
        countryListRMFromRealm.forEach {
            countryListDMFromRealm.add(RealmModelTypeConverter.getCountryDataModel(it))
        }
        countryListDMFromRealm.add(1)

        return countryListDMFromRealm

    }

    // write countries to realm
    fun writeCountriesToRealm(data: List<Data>) {
        data.forEach {
            theRealm.executeTransaction{ theRealm ->
                theRealm.copyToRealmOrUpdate(RealmModelTypeConverter.getCountryRealmModel(it))
            }
        }
    }

    // gets a country from realm by its code
    fun getSingleCountry(countryItemCode: String) : Data {
        val countryRM = theRealm.where<DataRM>().equalTo("code", countryItemCode).findFirst()
        return RealmModelTypeConverter.getCountryDataModel(countryRM!!)

    }

}