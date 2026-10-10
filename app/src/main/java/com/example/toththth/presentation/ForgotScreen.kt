package com.example.toththth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPassword(
    onNavigateBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var email by remember { mutableStateOf("") }
    var emailSent by remember { mutableStateOf(false) }
    var snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when(val state = uiState){
            is AuthState.EmailSent -> {
                emailSent = true
                viewModel.reset()
            }
            is AuthState.Error -> {
                snackBarHostState.showSnackbar(state.message)
                viewModel.reset()
                }

            else -> Unit

        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reset Password") },
                navigationIcon = {
                    IconButton( onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!emailSent) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it},
                    label = { Text("Email")},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(Modifier.height(16.dp))
                val isLoading = uiState is AuthState.Loading
                Button(
                    onClick = { viewModel.sendPasswordReset(email) },
                    enabled = !isLoading && email.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoading){
                        CircularProgressIndicator(strokeWidth = 2.dp)
                    } else {
                        Text("Sent Reset Email")
                    }
                    Spacer(Modifier.height(16.dp))
                }
            } else {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                )
                Spacer(Modifier.height(16.dp))
                Text("Check the inbox of this email $email", textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))

                Button(onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth()) {
                    Text("Go Back to login")
                }
            }
        }
    }
}








