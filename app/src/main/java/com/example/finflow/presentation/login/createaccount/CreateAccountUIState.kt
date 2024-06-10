package com.example.finflow.presentation.login.createaccount

data class CreateAccountUIState(
    val email: String = "",
    val isEmailValid: Boolean = true,
    val emailError: String = "",
    val password: String = "",
    val isPasswordValid: Boolean = true,
    val passwordError: String = "",
    val confirmPassword: String = "",
    val isConfirmPasswordValid: Boolean = true,
    val confirmPasswordError: String = "",
    val name: String = "",
    val isNameValid: Boolean = true,
    val nameError: String = "",
    val isLoading: Boolean = false
)