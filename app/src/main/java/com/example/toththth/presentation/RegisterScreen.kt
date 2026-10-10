package com.example.toththth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun  RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?> (null) }
    var passwordError by remember { mutableStateOf<String?>(null)  }
    var passwordVisibility by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var snackBarHostState = remember { SnackbarHostState() }
    var confirmPasswordError by remember { mutableStateOf<String?> (null) }

    LaunchedEffect(uiState) {
        when (val state = uiState){
            is AuthState.Success -> {
                onRegisterSuccess()
                viewModel.reset()
            }
            is AuthState.Error -> {
                val message = state.message
                when{
                    message.contains("email", ignoreCase = true) -> {
                        emailError = message
                    }
                    message.contains("password", ignoreCase = true) ||
                        message.contains("character", ignoreCase = true) -> {
                            passwordError = message
                    }
                    else -> {
                        snackBarHostState.showSnackbar(message)
                    }
                }
            }
            else -> Unit
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it
                    emailError = null},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email")},
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError != null,
                supportingText = {
                    if (emailError != null)
                        Text(emailError!!,
                            color = MaterialTheme.colorScheme.error)
                }
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = passwordError != null,
                supportingText = {
                    if (passwordError != null){
                        Text(passwordError!!,
                            color = MaterialTheme.colorScheme.error)
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility}) {
                        Icon(
                            imageVector = if (passwordVisibility) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = if (passwordVisibility) "Show password " else "Hide password"
                        )
                    }
                }
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it
                                confirmPasswordError = null },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Confirm password")},
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisibility){
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                isError = confirmPassword.isNotEmpty() && password != confirmPassword,
                supportingText = {
                    if (confirmPassword.isNotEmpty() && password != confirmPassword){
                        Text("Password don't match")
                    }
                },
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisibility = !passwordVisibility}
                    ) {
                        Icon(
                            imageVector = if (passwordVisibility){
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = if (passwordVisibility){
                                "show password"
                            } else {
                                "Hide password"
                            }
                        )
                    }
                }
            )

            Spacer(Modifier.height(24.dp))

            val isLoading = uiState is AuthState.Loading
            val passwordMatch = password == confirmPassword && password.isNotEmpty()
            Button(
                onClick = {
                    if (passwordMatch)
                        viewModel.register(email, password,confirmPassword)
                },

                modifier = Modifier.fillMaxWidth(),
                enabled = email.isNotBlank() && password.isNotBlank() && password == confirmPassword
            ) {
                if (isLoading)
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                else
                    Text("Create account")
            }
            Spacer(Modifier.height(16.dp))
            TextButton( onClick = onNavigateToLogin ) {
                Text("Already have an account? Sign in")
            }
        }
    }
}








