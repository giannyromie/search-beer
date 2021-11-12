package com.example.beerproject.network

/**
 * Common class used by API responses.
</T> */
sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: "unknown error")
        }

        fun <T> create(body: T, code: Int): ApiResponse<T> {
            return if (body == null || code == 204) {
                ApiEmptyResponse()
            } else {
                ApiSuccessResponse(body)
            }
        }
    }
}

class ApiEmptyResponse<T> : ApiResponse<T>()
data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>() //HTTP 200 - 204 responses
data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>() // HTTP 400 - 500
