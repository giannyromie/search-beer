package com.example.beerproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * A beer entity
 */
@Entity
data class Beer (
    @PrimaryKey
    val id: Int,
    val name: String,
    val tagline: String,
    val description: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("abv")
    val alcoholByVolume: Double,
    @SerializedName("ibu")
    val bitterness: Double
) {}