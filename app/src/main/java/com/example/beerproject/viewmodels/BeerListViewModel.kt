package com.example.beerproject.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.beerproject.model.Beer
import com.example.beerproject.network.NetworkResource
import com.example.beerproject.repository.BeerListRepository

class BeerListViewModel @ViewModelInject constructor(
    private val beerListRepository: BeerListRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val _beers: MutableLiveData<NetworkResource<List<Beer>>> by lazy {
        MutableLiveData<NetworkResource<List<Beer>>>()
    }
    val beers: LiveData<NetworkResource<List<Beer>>>
        get() = _beers

    fun searchBeers(nameB: String): LiveData<NetworkResource<List<Beer>>> {
        return beerListRepository.searchBeers(nameB)
    }

    fun setBeerList(beers: NetworkResource<List<Beer>>) {
        this._beers.value = beers
    }

    fun getBeer(beerId: Int?): Beer? {
        return _beers.value?.data?.find { beer -> beer.id == beerId }
    }
}