package com.example.countries.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.R
import com.example.countries.RetrofitInstance
import com.example.countries.adapters.CountriesAdapter
import com.example.countries.databinding.FragmentHomePageBinding
import com.example.countries.helpers.SwipeToDelete
import com.example.countries.model.Country
import com.example.countries.model.Data
import com.example.countries.realm.RealmInstanceHelper
import com.example.countries.realm.RealmModelTypeConverter
import com.example.countries.realmDataModel.DataRM
import com.example.countries.sharedpreference.SharedPreferenceHelper
import com.example.countries.viewmodels.CountryViewModel
import io.realm.kotlin.where
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

const val TAG = "API_EXCEPTION"

class HomePageFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!
    lateinit var theAdapter : CountriesAdapter
    private val countryViewModel : CountryViewModel by activityViewModels()
    val theRealm by lazy { RealmInstanceHelper.getInstance() }
    val countryListPrefHelper by lazy { SharedPreferenceHelper(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        theAdapter = CountriesAdapter(requireContext(), this@HomePageFragment)
        theAdapter.notifyDataSetChanged()

        countryViewModel.swipeToDelete(binding.homePageRecycler, theAdapter)

        theAdapter.setOnItemClickListener(object : CountriesAdapter.OnCountryItemClickListener {
            override fun onItemClick(
                position: Int,
                recyclerViewList: ArrayList<Any>
            ) {
                val theAction = HomePageFragmentDirections.actionHomePageFragmentToDetailFragment((recyclerViewList[position] as Data).code)
                if (findNavController().currentDestination?.id == R.id.homePageFragment){
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
                (recyclerViewList[position] as Data).isSaved = isChecked
                theAdapter.notifyItemChanged(position)
            }
        })

        if (countryListPrefHelper.isRemoteApiDataFetched()) {

            // if remote api data is fetched and written to realm db, no longer fetch data via api and use realm db instead
            val countryListDMFromRealm = arrayListOf<Any>()
            val countryListRMFromRealm = theRealm.where<DataRM>().findAll()
            countryListRMFromRealm.forEach {
                countryListDMFromRealm.add(RealmModelTypeConverter.getCountryDataModel(it))
            }
            countryListDMFromRealm.add(1)
            theAdapter.setData(countryListDMFromRealm)

        } else {
            lifecycleScope.launchWhenCreated {
                binding.progressBar.visibility = View.VISIBLE

                val response = try {
                    RetrofitInstance.api.getCountries("0eb1f43abemsh74b672cd98d0c98p1090b9jsn051b067b6d16", "wft-geo-db.p.rapidapi.com")
                } catch (e: IOException) {
                    Log.e(TAG, "IOException: $e")
                    binding.progressBar.visibility = View.GONE
                    return@launchWhenCreated
                } catch (e: HttpException) {
                    Log.e(TAG, "HttpException: $e")
                    binding.progressBar.visibility = View.GONE
                    return@launchWhenCreated
                } catch (e: Exception) {
                    Log.e(TAG, "Exception: $e")
                    binding.progressBar.visibility = View.GONE
                    return@launchWhenCreated
                }

                if (response.isSuccessful && response.body() != null) {
                    val countryList = ArrayList<Any>(response.body()!!.data)
                    countryList.add(1)
                    theAdapter.setData(countryList)

                    writeToRealm(response.body()!!.data)
                    countryListPrefHelper.setRemoteApiDataFetched(true)

                } else {
                    Log.e(TAG, "Response Not Successfull")
                }
                binding.progressBar.visibility = View.GONE
            }
        }

        binding.homePageRecycler.apply {
            setHasFixedSize(true)
            adapter = theAdapter
        }
    }

    private fun writeToRealm(data: List<Data>) {
        data.forEach {
            theRealm.executeTransaction{ theRealm ->
                theRealm.copyToRealmOrUpdate(RealmModelTypeConverter.getCountryRealmModel(it))
            }
        }
    }

}