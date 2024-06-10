package com.example.finflow.presentation.destinations

import kotlinx.serialization.Serializable

sealed class Destinations(val route: String) {
    object attentionList : Destinations("attentionList")
    object feedbackForm : Destinations("feedbackForm")
}

@Serializable
object NavTest

@Serializable
object NavCreateAccountEmailScreen

@Serializable
object NavCreateAccountPasswordScreen

@Serializable
object NavVerificationSuccessScreen

@Serializable
object NavLoginScreen

@Serializable
object NavLoginSuccessScreen

@Serializable
object NavOnBoardingScreen

@Serializable
data class NavEmailVerificationScreen(
    val email: String,
    val password: String
)

@Serializable
object NavForgotPassword