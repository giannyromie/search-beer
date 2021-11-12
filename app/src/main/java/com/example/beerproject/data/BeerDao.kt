package com.example.beerproject.data

import androidx.lifecycle.LiveData
import com.example.beerproject.model.Beer

/**
 * Data Layer
 */
interface BeerDao {
    fun insert(vararg beers: Beer)
    fun insert(beers: List<Beer>)
    fun delete()
    fun update(beer: Beer)
    fun load(beerId: Int): Beer?
    fun load(): LiveData<List<Beer>>
}