package com.example.toththth.data

import android.content.Context
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun register(email: String, password: String): Result<FirebaseUser>
    suspend fun signInWithGoogle(context: Context): Result<FirebaseUser>
    suspend fun sendPasswordReset (email: String): Result<Unit>
    suspend fun logOut()

}
