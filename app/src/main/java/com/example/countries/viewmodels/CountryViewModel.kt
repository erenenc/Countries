package com.example.countries.viewmodels

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.adapters.CountriesAdapter
import com.example.countries.helpers.SwipeToDelete
import com.example.countries.model.Data
import com.example.countries.realm.RealmInstanceHelper
import com.example.countries.realmDataModel.DataRM
import io.realm.kotlin.where

class CountryViewModel : ViewModel() {

    val theRealm by lazy { RealmInstanceHelper.getInstance() }

    fun swipeToDelete(recyclerView: RecyclerView, theAdapter: CountriesAdapter) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = theAdapter.countryList[viewHolder.adapterPosition] as Data
                deleteCountryFromRealm(deletedItem)

                theAdapter.countryList.removeAt(viewHolder.layoutPosition)
                theAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun deleteCountryFromRealm(deletedItem: Data) {
        theRealm.executeTransaction { theRealm ->
            theRealm.where<DataRM>().equalTo("code", deletedItem.code).findFirst().let {
                it?.deleteFromRealm()
            }
        }
    }
}