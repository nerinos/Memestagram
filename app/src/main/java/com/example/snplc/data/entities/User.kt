package com.example.snplc.data.entities

import com.example.snplc.other.Constants.DEFAULT_PROFILE_PICTURE_URL
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User (
    val uid: String = "", // default value is needed for firestore
    val username: String = "",
    val description: String = "",
    val profilePictureUrl: String = DEFAULT_PROFILE_PICTURE_URL,
    var follows: List<String> = listOf(),
    @Exclude // we don't need this in firebase, only locally
    var isFollowing: Boolean = false
)
