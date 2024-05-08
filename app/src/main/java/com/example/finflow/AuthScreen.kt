package com.example.finflow

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.finflow.R

@Composable
fun AuthScreen(onLogin: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.bannericon),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = "Journal App",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        //In this code, `AuthScreen` is a Composable function that takes a lambda `onLogin` as a parameter. This lambda is called when the "Sign In" button is clicked. The `email` and `password` are state variables that hold the current text of the email and password fields. The `OutlinedTextField` Composable is used to create the email and password fields, and the `TextButton` Composable is used to create the "Sign In" and "Sign Up" buttons.
        TextButton(
        onClick = { onLogin(email, password) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
        ) {
        Text("Sign In")
    }

        TextButton(
            onClick = { /* Handle sign up here */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Sign Up")
        }
    }
}