package com.example.core.util

import android.content.Context
import com.example.core.extensions.hasInternetConnection
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHelper @Inject constructor(@ApplicationContext private val context: Context) :
    BaseNetworkHelper {
    @Override
    override fun hasInternetConnection() = context.hasInternetConnection()
}

interface BaseNetworkHelper {
    fun hasInternetConnection(): Boolean
}