package com.example.finflow.presentation

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finflow.MainViewModel
import com.example.finflow.presentation.destinations.Destinations
import com.example.finflow.presentation.login.ForgotPassword
import com.example.finflow.presentation.login.SignInUI
import com.example.finflow.presentation.login.SignInUsingEmail
import com.example.finflow.presentation.screens.MainScreen
import com.example.finflow.presentation.ui.theme.AppTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alreadySignedIn = FirebaseAuth.getInstance().currentUser != null
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val viewModel = MainViewModel()
                val navController = rememberNavController()
                NavHost(
                    navController = navController, startDestination = if (alreadySignedIn)
                        Destinations.MainScreen.route else Destinations.SignIn.route
                ) {

                    composable(Destinations.SignIn.route) {
                        SignInUI(navController = navController)
                    }
                    composable(Destinations.EmailSignIn.route) {
                        SignInUsingEmail(navController = navController)
                    }
                    composable(Destinations.ForgotPassword.route) {
                        ForgotPassword(navController = navController)
                    }
                    composable(Destinations.MainScreen.route) {
                        MainScreen(navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

fun signInToDashboard(context: Context){
    val intent = Intent(context, StartActivity::class.java)
    context.startActivity(intent)
}
