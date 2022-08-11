package com.example.countries.fragments

import android.content.Intent
import android.graphics.Insets.add
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.caverock.androidsvg.SVG
import com.example.countries.MainActivity
import com.example.countries.RetrofitInstance
import com.example.countries.databinding.FragmentDetailBinding
import com.example.countries.model.CountryDetail
import com.example.countries.model.Data
import com.example.countries.realm.RealmInstanceHelper
import com.example.countries.realmDataModel.DataRM
import io.realm.kotlin.where
import retrofit2.HttpException
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val countryCode by lazy { DetailFragmentArgs.fromBundle(requireArguments()).code }
    private var wikiDataId = ""
    val theRealm by lazy { RealmInstanceHelper.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            binding.progressBarDetail.visibility = View.VISIBLE

            val response = try {
                RetrofitInstance.api.getSpecificCountry(countryCode,"0eb1f43abemsh74b672cd98d0c98p1090b9jsn051b067b6d16", "wft-geo-db.p.rapidapi.com")
            } catch (e: IOException) {
                Log.e(TAG, "IOException: $e")
                binding.progressBarDetail.visibility = View.GONE
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException: $e")
                binding.progressBarDetail.visibility = View.GONE
                return@launchWhenCreated
            } catch (e: Exception) {
                Log.e(TAG, "Exception: $e")
                binding.progressBarDetail.visibility = View.GONE
                return@launchWhenCreated
            }

            if (response.isSuccessful && response.body() != null) {
                showDetails(response.body()!!)
            } else {
                Log.e(TAG, "Response Not Successfull")
            }
            binding.progressBarDetail.visibility = View.GONE
        }

        (requireActivity() as MainActivity).binding.toolbarBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.countryDetailButton.setOnClickListener {

            val url = "https://www.wikidata.org/wiki/$wikiDataId"
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            } catch (e: java.lang.Exception) {
                Toast.makeText(requireContext(), "URL not found!!", Toast.LENGTH_LONG).show()
            }
        }

        (requireActivity() as MainActivity).binding.toolbarStar.setOnCheckedChangeListener { buttonview, isChecked ->

            if (buttonview.isPressed) {
                theRealm.executeTransaction { theRealm ->
                    val country = theRealm.where<DataRM>()
                        .equalTo("code", countryCode)
                        .findFirst()
                    country?.isSaved = isChecked
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val theCountry = theRealm.where<DataRM>().equalTo("code", countryCode).findFirst()
        (requireActivity() as MainActivity).binding.toolbarStar.visibility = View.VISIBLE
        (requireActivity() as MainActivity).binding.toolbarStar.isChecked = theCountry?.isSaved!!
        (requireActivity() as MainActivity).binding.toolbarBackArrow.visibility = View.VISIBLE
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as MainActivity).binding.toolbarStar.visibility = View.GONE
        (requireActivity() as MainActivity).binding.toolbarBackArrow.visibility = View.GONE

    }

    private fun showDetails(countryDetail: CountryDetail) {

        binding.countryDetailText2.text = countryDetail.data.code
        wikiDataId = countryDetail.data.wikiDataId

        lifecycleScope.launchWhenCreated {
            binding.progressBarDetailImage.visibility = View.VISIBLE
            try {
                binding.countryDetailImage.loadSvg(countryDetail.data.flagImageUri!!)
            } catch (e: Exception) {
                binding.progressBarDetailImage.visibility = View.GONE
                Toast.makeText(requireContext(), "Flag not found!!", Toast.LENGTH_LONG).show()
            }
            binding.progressBarDetailImage.visibility = View.GONE
        }
    }

    fun ImageView.loadSvg(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }

}