package com.example.countries.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.adapters.CountriesAdapter
import com.example.countries.helpers.SwipeToDelete
import com.example.countries.model.Data
import com.example.countries.realm.CountryDao
import com.example.countries.realm.RealmInstanceHelper
import com.example.countries.realmDataModel.DataRM
import com.example.countries.repository.CountryRepository
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountryViewModel : ViewModel() {

    private val repository: CountryRepository = CountryRepository(CountryDao)

    fun swipeToDelete(recyclerView: RecyclerView, theAdapter: CountriesAdapter) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = theAdapter.countryList[viewHolder.adapterPosition] as Data
                deleteItem(deletedItem.code)

                theAdapter.countryList.removeAt(viewHolder.layoutPosition)
                theAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun deleteItem(countryItemCode: String) {
        repository.deleteItem(countryItemCode)
    }

    fun updateItem(countryItemCode: String, isChecked: Boolean) {
        repository.updateItem(countryItemCode, isChecked)
    }

    fun getAllSavedCountries() : ArrayList<Any> {
        return repository.getAllSavedCountries()
    }

    fun getAllCountries() : ArrayList<Any> {
        return repository.getAllCountries()
    }

    fun writeCountriesToRealm(data: List<Data>) {
        repository.writeCountriesToRealm(data)
    }

    fun getSingleCountry(countryItemCode: String) : Data {
        return repository.getSingleCountry(countryItemCode)
    }

}