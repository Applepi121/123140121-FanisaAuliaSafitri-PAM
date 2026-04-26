package com.fanisa.upgradenote.di

import com.fanisa.upgradenote.data.platform.DeviceInfo
import com.fanisa.upgradenote.data.platform.NetworkMonitor
import org.koin.dsl.module

actual val platformModule = module {
    single { NetworkMonitor() }
    single { DeviceInfo() }
}