package com.example.beerproject.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.beerproject.model.Beer

class BeerDetailViewModel @ViewModelInject constructor(
    @Assisted private val state: SavedStateHandle
): ViewModel() {
    private val _beer: MutableLiveData<Beer> by lazy {
        MutableLiveData<Beer>()
    }
    val beer: LiveData<Beer>
        get() = _beer

    fun setBeer(beer: Beer?) {
        this._beer.value = beer
    }
}