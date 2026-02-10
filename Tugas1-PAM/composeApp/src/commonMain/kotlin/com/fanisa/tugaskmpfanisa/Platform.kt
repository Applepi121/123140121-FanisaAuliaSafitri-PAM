package com.fanisa.tugaskmpfanisa

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform