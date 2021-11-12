package com.example.beerproject.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.beerproject.model.Beer

/**
 * Beer Database Room implementation
 */
@Database(entities = [Beer::class], version = 1, exportSchema = false)
abstract class BeerRoomDatabase: BeerDatabase, RoomDatabase() {
    abstract override fun beerDao(): BeerRoomDao
}