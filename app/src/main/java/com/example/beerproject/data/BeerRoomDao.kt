package com.example.beerproject.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.*
import com.example.beerproject.model.Beer

/**
 * How to access data with Room DAO
 */
@Dao
abstract class BeerRoomDao: BeerDao {
    @Insert(onConflict = ABORT)
    abstract override fun insert(vararg beers: Beer)

    @Insert(onConflict = IGNORE)
    abstract override fun insert(beers: List<Beer>)

    @Query("DELETE FROM Beer")
    abstract override fun delete()

    @Update
    abstract override fun update(beer: Beer)

    @Query("SELECT * FROM Beer WHERE id = :beerId")
    abstract override fun load(beerId: Int): Beer?

    @Query("SELECT * FROM Beer")
    abstract override fun load(): LiveData<List<Beer>>
}