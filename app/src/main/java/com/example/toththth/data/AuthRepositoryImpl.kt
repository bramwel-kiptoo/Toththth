package com.example.toththth.data

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.toththth.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val credentialManager: CredentialManager,
): AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password)
                .await()
            Result.success(result.user!!)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun register(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email , password )
                .await()
            Result.success(result.user!!)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(context: Context): Result<FirebaseUser> {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.web_client_id))
                .setAutoSelectEnabled(false)
                .build()
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
            val response = credentialManager.getCredential(
                context = context,
                request = request
            )
            val credential = response.credential
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val tokenId = googleIdTokenCredential.idToken

                val firebaseCredential = GoogleAuthProvider.getCredential(tokenId, null)
                val result = firebaseAuth.signInWithCredential(firebaseCredential)
                    .await()
                Result.success(result.user!!)
            } else {
                Result.failure(Exception("Unexpected credential"))
            }
        } catch (e: GetCredentialCancellationException) {
            Result.failure(Exception("Sign in Cancelled"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> {
        return try {
             firebaseAuth.sendPasswordResetEmail(email)
                 .await()
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun logOut() {
        firebaseAuth.signOut()
        try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (e: Exception) {
            // Ignore clear errors
        }
    }
}
