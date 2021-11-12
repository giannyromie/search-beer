package com.example.beerproject.data

/**
 * Store information in beer database.
 */
interface BeerDatabase {
    fun beerDao(): BeerDao
}