package com.fanisa.profileapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform