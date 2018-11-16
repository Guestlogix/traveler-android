package com.guestlogix.travelercorekit.models

data class AuthTokenRequestObject(
    val applicationId: String,
    val deviceId: String,
    val language: String,
    val locale: String,
    val osVersion: String,
    val region: String
)