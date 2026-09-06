package com.example.tamarasdk

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform