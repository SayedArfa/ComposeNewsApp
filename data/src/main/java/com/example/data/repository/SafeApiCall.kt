package com.example.data.repository

import com.example.core.ErrorType
import com.example.core.Result
import com.example.core.util.BaseNetworkHelper
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SafeApiCall @Inject constructor(private val networkHelper: BaseNetworkHelper) {
    suspend operator fun <RESPONSE> invoke(
        coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall: suspend () -> Response<RESPONSE>
    ): Result<RESPONSE> {
        return withContext(coroutineDispatcher) {
            var retryCount = -1
            if (!networkHelper.hasInternetConnection()) {
                Result.Error(ErrorType.NoInternetError)
            } else {
                var result: Result<RESPONSE>
                do {
                    retryCount++
                    result = callApi(apiCall)
                } while (result is Result.Error && (result.errorType is ErrorType.NetworkError || ((result.errorType as? ErrorType.ApiError)?.statusCode == 401)) && retryCount < 1)
                result
            }
        }
    }

    private suspend fun <RESPONSE> callApi(
        apiCall: suspend () -> Response<RESPONSE>,
    ): Result<RESPONSE> {
        return try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Error(ErrorType.UnknownError(null))
            } else {
                Result.Error(
                    ErrorType.ApiError(
                        response.code(),
                        response.errorBody()?.string()
                    )
                )
            }

        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> {
                    Result.Error(ErrorType.NetworkError(throwable))
                }

                is JsonParseException -> {
                    Result.Error(ErrorType.JsonParseError(throwable))
                }

                else -> {
                    Result.Error(ErrorType.UnknownError(throwable))
                }
            }
        }
    }
}