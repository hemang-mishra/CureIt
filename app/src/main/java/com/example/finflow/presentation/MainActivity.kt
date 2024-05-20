package com.example.finflow.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.finflow.presentation.ui.theme.AppTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alreadySignedIn = FirebaseAuth.getInstance().currentUser != null
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()

            }
        }
    }
}

fun signInToDashboard(context: Context){
    val intent = Intent(context, StartActivity::class.java)
    context.startActivity(intent)
}
