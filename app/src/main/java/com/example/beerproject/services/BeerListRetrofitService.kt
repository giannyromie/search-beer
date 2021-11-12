package com.example.beerproject.services

import androidx.lifecycle.LiveData
import com.example.beerproject.model.Beer
import com.example.beerproject.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Beer list service Retrofit and OKhttp implementation
 */
interface BeerListRetrofitService : BeerListService {
    @GET("beers")
    override fun getBeersByName(
        @Query("beer_name") name: String
    ): LiveData<ApiResponse<List<Beer>>>

}