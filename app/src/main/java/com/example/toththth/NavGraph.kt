package com.example.toththth

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.toththth.presentation.AuthViewModel
import com.example.toththth.presentation.ForgotPassword
import com.example.toththth.presentation.HomeScreen
import com.example.toththth.presentation.LoginScreen
import com.example.toththth.presentation.RegisterScreen

@Composable
fun NavGraph( ){
    val navController = rememberNavController()
    val viewModel: AuthViewModel = hiltViewModel()
    val startDestination = if (viewModel.isLoggedIn) "home" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login"){
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true}
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToForgotScreen = {
                    navController.navigate("forgot_screen")
                }
            )
        }
        composable("register"){
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true}
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable("home"){
            HomeScreen(
                onLogOut = {
                    navController.navigate("login"){
                        popUpTo(0) {inclusive = true}
                    }
                }
            )
        }
        composable("forgot_screen"){
            ForgotPassword(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
