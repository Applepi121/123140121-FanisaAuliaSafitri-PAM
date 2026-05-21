package com.fanisa.upgradenote

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform