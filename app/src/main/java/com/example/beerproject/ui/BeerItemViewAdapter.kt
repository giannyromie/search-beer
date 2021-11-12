package com.example.beerproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.beerproject.R
import com.example.beerproject.model.Beer

/**
 * Adapter
 */
class BeerItemViewAdapter(
    private val parentActivity: MainActivity,
    private val values: List<Beer>,
    private val moveFragment: Boolean
) :
    RecyclerView.Adapter<BeerItemViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        navigateFragment(v)
    }

    private fun navigateFragment(v: View) {
        val item = v.tag as Beer
        val args = Bundle().apply {
            putString(BeerInformationFragment.ITEM_ID, item.id.toString())
        }
        if (moveFragment) {
            val fragment = BeerInformationFragment()
                .apply {
                    arguments = args
                }
            parentActivity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.beer_detail_container, fragment)
                .commit()
        } else {
            findNavController(
                parentActivity,
                R.id.frNavHost
            ).navigate(R.id.action_beerListFragment_to_beerDetailFragment, args)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.beers_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.txtBeerName.text = item.name
        holder.txtBeerTagline.text = item.tagline
        holder.txtAlcoholByVolume.text = item.alcoholByVolume.toString()
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .into(holder.imgBeer);
        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtBeerName: TextView = view.findViewById(R.id.txt_beer_name_list)
        val txtBeerTagline: TextView = view.findViewById(R.id.txt_beer_list_tagline)
        val txtAlcoholByVolume: TextView = view.findViewById(R.id.txt_beer_list_alcohol_by_volume)
        val imgBeer: ImageView = view.findViewById(R.id.beer_img)
    }
}