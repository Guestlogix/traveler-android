package com.guestlogix.travelercorekit.models

data class AuthTokenResponseObject(
    val created: String,
    val expires: String,
    val token: String
)