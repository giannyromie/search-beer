package com.example.beerproject.services

import androidx.lifecycle.LiveData
import com.example.beerproject.model.Beer
import com.example.beerproject.network.ApiResponse

/**
 * Interface that allow to handle http calls to beers backend side
 */
interface BeerListService {
    /**
     * @GET Method. Retrieves beers by name
     */
    fun getBeersByName(name: String): LiveData<ApiResponse<List<Beer>>>
}