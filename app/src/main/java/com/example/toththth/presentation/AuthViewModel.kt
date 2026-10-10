package com.example.toththth.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toththth.data.AuthRepository
import com.example.toththth.data.Validation
import com.example.toththth.domain.ErrorFromFirebase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel(){
     private val _uiState = MutableStateFlow<AuthState>(AuthState.Idle)
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    val isLoggedIn: Boolean
        get() = authRepository.currentUser != null

    fun login(email: String, password: String){
        val emailValidation = Validation.validateEmail(email)
        if (!emailValidation.isValid){
            _uiState.value = AuthState.Error(
                emailValidation.errorMessage ?: "Invalid email"
            )
        }
        val passwordValidation = Validation.validatePassword(password)
        if (!passwordValidation.isValid){
            _uiState.value = AuthState.Error(
                passwordValidation.errorMessage ?: "Invalid password"
            )
        }
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            val result = authRepository.login(email, password)
            _uiState.value = result.fold(
                onSuccess = { user ->
                    AuthState.Success(user)
                },
                onFailure = { e ->
                    AuthState.Error(ErrorFromFirebase.possibleError(e as Exception))
                }
            )
        }
    }
    fun register(email: String, password: String, confirmPassword: String){
        val emailValidation = Validation.validateEmail(email)
        if (!emailValidation.isValid){
            _uiState.value = AuthState.Error(
                emailValidation.errorMessage ?: "Invalid email"
            )
        }
        val passwordValidation = Validation.validatePassword(password)
        if (!passwordValidation.isValid){
            _uiState.value = AuthState.Error(
                passwordValidation.errorMessage ?: "Invalid password"
            )
        }
        val confirmPasswordValidation = Validation.confirmPasswordValidation(password, confirmPassword)
        if (!confirmPasswordValidation.isValid){
            _uiState.value = AuthState.Error(
                confirmPasswordValidation.errorMessage ?: "Confirm password invalid"
            )
        }
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            val result = authRepository.register(email, password)
            _uiState.value = result.fold(
                onSuccess = { user ->
                    AuthState.Success(user)
                },
                onFailure = { exception ->
                    AuthState.Error(ErrorFromFirebase.possibleError(exception as Exception))
                }
            )
        }
    }
    fun signInWithGoogle(context: Context){
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            val result = authRepository.signInWithGoogle(context)
            _uiState.value = result.fold(
                onSuccess = { user ->
                    AuthState.Success(user)
                            },
                onFailure = { e ->
                    AuthState.Error(ErrorFromFirebase.possibleError(e as Exception))
                }
            )
        }
    }
    fun sendPasswordReset(email: String){
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            val result = authRepository.sendPasswordReset(email)
            _uiState.value = result.fold(
                onSuccess = { AuthState.EmailSent},
                onFailure = { e ->
                    AuthState.Error(ErrorFromFirebase.possibleError(e as Exception)) }
            )
        }
    }
    fun logOut(){
        viewModelScope.launch {
            authRepository.logOut()
            reset()
        }
    }
    fun reset(){
        _uiState.value = AuthState.Idle
    }
}
sealed class AuthState{
    object EmailSent: AuthState()
    object Idle: AuthState()
    object Loading: AuthState()
    data class Success(val user: FirebaseUser): AuthState()
    data class Error(val message: String): AuthState()
}







