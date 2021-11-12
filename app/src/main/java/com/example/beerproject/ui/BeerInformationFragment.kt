package com.example.beerproject.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.beerproject.R
import com.example.beerproject.model.Beer
import com.example.beerproject.viewmodels.BeerDetailViewModel
import com.example.beerproject.viewmodels.BeerListViewModel
import kotlinx.android.synthetic.main.beer_detail.*

/**
 * A fragment that contains information about selected beer
 */
class BeerInformationFragment : Fragment(R.layout.beer_detail) {

    private val beerListViewModel: BeerListViewModel by activityViewModels()
    private val beerDetailViewModel: BeerDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ITEM_ID)) {
                val beerId = it.getString(ITEM_ID)?.toInt()

                beerDetailViewModel.beer.observe(this) { beer ->
                    updateBeerInformation(beer)
                }

                beerDetailViewModel.setBeer(beerListViewModel.getBeer(beerId))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateBeerInformation(beer: Beer) {
        activity?.toolbar_layout?.title = beer.name
        Glide.with(this)
            .load(beer.imageUrl)
            .into(beer_img);

        txt_beer_description.text = beer.description
        txt_beer_amount_alcohol_by_volume.text =
            getString(R.string.alcohol_by_olume).plus(beer.alcoholByVolume.toString())
        txt_beer_bitterness.text =
            getString(R.string.alcohol_and_bitterness).plus(beer.bitterness.toString())
        txt_beer_name.text = beer.name
    }

    companion object {
        const val ITEM_ID = "item_id" // ID Fragment
    }
}