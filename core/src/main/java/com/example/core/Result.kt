package com.example.core

import java.io.IOException

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val errorType: ErrorType) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

sealed class ErrorType {
    data class ApiError(val statusCode: Int, val errorBody: String?) : ErrorType()
    data object NoInternetError : ErrorType()
    data class NetworkError(val exception: IOException) : ErrorType()
    data class JsonParseError(val exception: Throwable?) : ErrorType()
    data class UnknownError(val exception: Throwable?) : ErrorType()
}