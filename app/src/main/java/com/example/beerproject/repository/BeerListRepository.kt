package com.example.beerproject.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.beerproject.data.BeerDao
import com.example.beerproject.helper.AppExecutors
import com.example.beerproject.model.Beer
import com.example.beerproject.network.NetworkBoundResource
import com.example.beerproject.network.NetworkResource
import com.example.beerproject.services.BeerListService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data management abstraction layer for beers
 */
@Singleton
class BeerListRepository @Inject constructor(
    private val beerListService: BeerListService,
    private val beerDao: BeerDao,
    private val appExecutors: AppExecutors
) {
    /**
     * search beers by name
     */
    fun searchBeers(name: String): MutableLiveData<NetworkResource<List<Beer>>> {
        return object : NetworkBoundResource<List<Beer>, List<Beer>>(appExecutors) {
            override fun storageResultDb(result: List<Beer>) {
                beerDao.delete() // clear store and insert data to search in memory
                beerDao.insert(result)
            }

            override fun needFetch(data: List<Beer>?): Boolean = true  // from network

            override fun getFromDb() = beerDao.load()

            override fun callApiRest() = beerListService.getBeersByName(name)

        }.asMutableLiveData()
    }
}