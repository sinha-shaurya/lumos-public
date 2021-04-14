package com.example.lumos.utils

/*
Enum class to get Loading Status for API calls
 */
enum class LoadingStatus {
    //When network request returns a successful response
    SUCCESS,

    //When network request is loading
    LOADING,

    //When network request fails
    FAILURE
}