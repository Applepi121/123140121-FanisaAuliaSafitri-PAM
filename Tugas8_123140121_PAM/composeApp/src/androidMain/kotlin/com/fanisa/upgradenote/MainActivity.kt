package com.fanisa.upgradenote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fanisa.upgradenote.di.commonModule
import com.fanisa.upgradenote.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            startKoin {
                androidContext(this@MainActivity)
                modules(commonModule, platformModule)
            }
        } catch (e: Exception) {}

        setContent { App(viewModel = getViewModel()) }
    }
}