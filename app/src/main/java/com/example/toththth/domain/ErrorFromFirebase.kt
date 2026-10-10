package com.example.toththth.domain

import com.google.firebase.auth.FirebaseAuthException

object ErrorFromFirebase  {
    fun possibleError(exception: Exception): String{
        if (exception !is FirebaseAuthException){
            return exception.localizedMessage ?: "Something went wrong"
        } else {
            return when(exception.errorCode){

                "ERROR_INVALID_EMAIL" -> "Please enater a valid email address"
                "ERROR_WRONG_PASSWORD" -> "Wrong password"
                "ERROR__USER_NOT_FOUND" -> "No account found with this email"
                "ERROR_USER_DISABLED" -> "This account has been disabled"
                "ERROR_TO_MANY_REQUESTS" -> "Too many failed attempts. please try again later"
                "ERROR_INVALID_CREDENTIAL" -> "Invalid credentials. Please try again "
                "ERROR_EMAIL_ALREADY_IN_USE" -> "An account with this email already exists"
                "ERROR_WEAK_PASSWORD" -> "Password is too weak. Use atleast 6 characters"
                "ERROR_USER_NOT_FOUND" -> "No account found with this email"
                "ERROR_NETWORK_REQUEST_FAILED" -> "Network error. Check your connection"

                else -> {
                    exception.localizedMessage ?: "Authentication failed. Please try again"
                }
            }
        }
    }
}