package com.example.lumos.utils

enum class LoginStatus {
    //user is logged in
    SUCCESS,

    //login request has failed
    FAILURE,

    //login request in progress
    LOADING,

    //user has not attempted login
    NOT_LOGGED_IN
}