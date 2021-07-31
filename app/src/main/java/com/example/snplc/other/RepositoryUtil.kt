package com.example.snplc.other

import com.google.firebase.firestore.FirebaseFirestoreException

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T>{// inline to prevent fun allocation
    return try {
        action()
    }  catch (e: FirebaseFirestoreException) {
        Resource.Error("Firebase error")
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage ?: "Unknown error occured")
    }
}