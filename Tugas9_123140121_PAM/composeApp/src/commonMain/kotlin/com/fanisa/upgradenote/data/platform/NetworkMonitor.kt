package com.fanisa.upgradenote.data.platform

import kotlinx.coroutines.flow.Flow

expect class NetworkMonitor {
    fun isConnected(): Boolean
    fun observeConnectivity(): Flow<Boolean>
}