package com.example.countries.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.databinding.CountryItemBinding
import com.example.countries.databinding.EmptyItemFeedBinding
import com.example.countries.model.Data

const val COUNTRY_ITEM_TYPE = 1
const val EMPTY_ITEM_TYPE = 2

class CountriesAdapter(private val context: Context,
                       private val theFragment : Fragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var countryList = arrayListOf<Any>()
    private lateinit var mListener: OnCountryItemClickListener

    class EmptyViewHolder(val binding: EmptyItemFeedBinding) : RecyclerView.ViewHolder(binding.root)

    class CountryItemViewHolder (val binding: CountryItemBinding, listener: OnCountryItemClickListener, countryItemsList: ArrayList<Any>) :  RecyclerView.ViewHolder(binding.root) {

        init {

            binding.countryItemContainer.setOnClickListener {
                listener.onItemClick(layoutPosition, countryItemsList)
            }

            binding.countryItemStar.apply {
                setOnCheckedChangeListener { buttonView, isChecked ->
                    if(buttonView.isPressed) {
                        listener.setOnCheckedChangeListener(layoutPosition, isChecked, countryItemsList)
                    }
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            COUNTRY_ITEM_TYPE -> {
                return CountryItemViewHolder(
                    CountryItemBinding.inflate(
                        LayoutInflater.from(context),
                        parent, false), mListener, countryList)
            }
            EMPTY_ITEM_TYPE -> {
                return EmptyViewHolder(
                    EmptyItemFeedBinding.inflate(
                        LayoutInflater.from(context),
                        parent, false))
            }
            else -> {
                return CountryItemViewHolder(
                    CountryItemBinding.inflate(
                        LayoutInflater.from(context),
                        parent, false), mListener, countryList)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder) {
            is CountryItemViewHolder -> {
                holder.binding.countryItemName.text = (countryList[position] as Data).name

                holder.binding.countryItemStar.apply {
                    isChecked = (countryList[position] as Data).isSaved
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            countryList[position] is Int -> {
                EMPTY_ITEM_TYPE
            }
            countryList[position] is Data -> {
                COUNTRY_ITEM_TYPE
            }
            else -> {
                COUNTRY_ITEM_TYPE
            }
        }
    }

    fun setOnItemClickListener(listener: OnCountryItemClickListener) {
        mListener = listener
    }

    fun setData(countryItemsList: ArrayList<Any>){
        this.countryList = countryItemsList
    }

    interface OnCountryItemClickListener {
        fun onItemClick(position: Int, recyclerViewList: ArrayList<Any>)
        fun setOnCheckedChangeListener(position: Int, isChecked: Boolean, recyclerViewList: ArrayList<Any>)
    }

}