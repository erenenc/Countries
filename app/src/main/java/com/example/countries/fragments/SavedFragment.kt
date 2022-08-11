package com.example.countries.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.countries.R
import com.example.countries.adapters.CountriesAdapter
import com.example.countries.databinding.FragmentSavedBinding
import com.example.countries.model.Data
import com.example.countries.realm.RealmInstanceHelper
import com.example.countries.realm.RealmModelTypeConverter
import com.example.countries.realmDataModel.DataRM
import com.example.countries.viewmodels.CountryViewModel
import io.realm.kotlin.where


class SavedFragment : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    lateinit var theAdapter : CountriesAdapter
    private val countryViewModel : CountryViewModel by activityViewModels()
    val theRealm by lazy { RealmInstanceHelper.getInstance() }
    val savedCountryList = arrayListOf<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedCountryList.clear()

        val savedCountryRealmList = theRealm.where<DataRM>().equalTo("isSaved", true).findAll()
        savedCountryRealmList.forEach {
            savedCountryList.add(RealmModelTypeConverter.getCountryDataModel(it))
        }
        savedCountryList.add(1)

        theAdapter = CountriesAdapter(requireContext(), this@SavedFragment)
        theAdapter.setData(savedCountryList)
        theAdapter.notifyDataSetChanged()

        countryViewModel.swipeToDelete(binding.savedRecycler, theAdapter)

        theAdapter.setOnItemClickListener(object : CountriesAdapter.OnCountryItemClickListener {
            override fun onItemClick(
                position: Int,
                recyclerViewList: ArrayList<Any>
            ) {
                val theAction = SavedFragmentDirections.actionSavedFragmentToDetailFragment((recyclerViewList[position] as Data).code)
                if (findNavController().currentDestination?.id == R.id.savedFragment){
                    findNavController().navigate(theAction)
                }
            }

            override fun setOnCheckedChangeListener(
                position: Int,
                isChecked: Boolean,
                recyclerViewList: ArrayList<Any>
            ) {
                theRealm.executeTransaction { theRealm ->
                    val country = theRealm.where<DataRM>()
                        .equalTo("code", (recyclerViewList[position] as Data).code)
                        .findFirst()
                    country?.isSaved = isChecked

                }
                removeItemFromList(recyclerViewList[position] as Data)

            }

        })

        binding.savedRecycler.apply {
            setHasFixedSize(true)
            adapter = theAdapter
        }

    }

    fun removeItemFromList(theCountryItem: Data)  {

        var theItemToBeRemoved : Data? = null

        savedCountryList.forEachIndexed { _, any ->
            if(any is Data)
            {
                if (any == theCountryItem)
                {
                    theItemToBeRemoved = any
                }
            }
        }

        theItemToBeRemoved?.let {

            Handler(Looper.getMainLooper()).post {
                savedCountryList.remove(it)
                theAdapter.notifyDataSetChanged()
            }
        }
    }

}