package com.fanisa.tugas2_123140121

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform