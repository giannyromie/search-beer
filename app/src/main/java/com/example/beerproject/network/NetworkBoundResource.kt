package com.example.beerproject.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.example.beerproject.helper.AppExecutors

/**
 * This abstract class provide an easy interface to fetch resource from both the database
 * and the network. It works well with the Android architecture component. Most of the code is taken from this repo.
 * Github: https://github.com/Loriot-n/NetworkBoundResource.git
</R> */

abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<NetworkResource<ResultType>>()

    init {
        result.value = NetworkResource.loading(null)
        @Suppress("LeakingThis")
        val dbSource = getFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (needFetch(data)) {
                callNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(NetworkResource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: NetworkResource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun callNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = callApiRest()
        result.addSource(dbSource) { newData ->
            setValue(NetworkResource.loading(newData))
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    appExecutors.diskIO().execute {
                        storageResultDb(processResponse(response))
                        appExecutors.mainThread().execute {
                            result.addSource(getFromDb()) { newData ->
                                setValue(NetworkResource.success(newData))
                            }
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        result.addSource(getFromDb()) { newData ->
                            setValue(NetworkResource.success(newData))
                        }
                    }
                }
                is ApiErrorResponse -> {
                    fetchFailed()
                    result.addSource(dbSource) { newData ->
                        setValue(NetworkResource.error(response.errorMessage, newData))
                    }
                }
            }
        }
    }

    protected open fun fetchFailed() {}

    fun asMutableLiveData() = result as MutableLiveData<NetworkResource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract fun storageResultDb(result: RequestType)

    @MainThread
    protected abstract fun needFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun getFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun callApiRest(): LiveData<ApiResponse<RequestType>>
}
