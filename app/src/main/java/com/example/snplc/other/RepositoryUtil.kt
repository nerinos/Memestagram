package com.example.snplc.other

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T>{// inline to prevent fun allocation
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Unknown error occured")
    }
}