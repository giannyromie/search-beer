package com.example.beerproject.provide

import android.app.Application
import androidx.room.Room
import com.example.beerproject.data.BeerDao
import com.example.beerproject.data.BeerDatabase
import com.example.beerproject.data.BeerRoomDatabase
import com.example.beerproject.helper.RetrofitLiveDataCallAdapterFactory
import com.example.beerproject.model.Beer
import com.example.beerproject.services.BeerEntityDeserializer
import com.example.beerproject.services.BeerListRetrofitService
import com.example.beerproject.services.BeerListService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Dependency injection on Android with Hilt
 */
@Module
@InstallIn(ApplicationComponent::class)
object BeerModule {
    /**
     * Builds and returns the Gson converter
     */
    private fun getGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Beer::class.java, BeerEntityDeserializer())
            .setLenient()
            .create()
    }

    /**
     * Builds and returns the OkHttpClient
     */
    private fun getOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    /**
     * Builds and returns the Retrofit client
     *
     * @param baseUrl API endpoint
     */
    private fun getRetrofit(baseUrl: String, okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RetrofitLiveDataCallAdapterFactory())
            .build()
    }

    /**
     * Provides instantiation for API client interface
     */
    @Provides
    @Singleton
    fun provideBeerListService(): BeerListService {
        val gson = getGson()
        val okHttpClient = getOkHttpClient()
        val retrofit = getRetrofit("https://api.punkapi.com/v2/", okHttpClient, gson)
        return retrofit.create(BeerListRetrofitService::class.java)
    }

    /**
     * Provides instantiation for BeerDatabase interface
     */
    @Singleton
    @Provides
    fun provideDb(app: Application): BeerDatabase {
        return Room
            .databaseBuilder(app, BeerRoomDatabase::class.java, "beer.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    /**
     * Provides instantiation for BeerDao interface
     */
    @Singleton
    @Provides
    fun provideBeerDao(db: BeerDatabase): BeerDao {
        return db.beerDao()
    }
}