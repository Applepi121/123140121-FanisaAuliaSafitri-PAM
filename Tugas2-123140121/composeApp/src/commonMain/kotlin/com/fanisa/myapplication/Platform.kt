package com.fanisa.myapplication

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform