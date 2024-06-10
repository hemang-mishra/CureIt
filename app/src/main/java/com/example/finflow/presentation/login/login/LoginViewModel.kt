package com.example.finflow.presentation.login.login

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.finflow.R
import com.example.finflow.domain.AccountServiceImpl
import com.example.finflow.presentation.destinations.NavEmailVerificationScreen
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

class LoginViewModel(val auth: FirebaseAuth) : ViewModel() {
    var uiState = mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(newEmail: String) {
        uiState.value = uiState.value.copy(email = newEmail)
    }

    fun onEmailErrorStateChange(isError: Boolean, errorText: String) {
        uiState.value = uiState.value.copy(isEmailValid = !isError, emailError = errorText)
    }

    fun onPasswordErrorStateChange(isError: Boolean, errorText: String) {
        uiState.value = uiState.value.copy(isPasswordValid = !isError, passwordError = errorText)
    }

    fun onPasswordChange(newPassword: String) {
        uiState.value = uiState.value.copy(password = newPassword)
    }

    fun forgotPassword(
        snackbarHostState: SnackbarHostState,
        navController: NavController,
        onSuccess: () -> Unit
    ) {
        if (uiState.value.isLoading) return
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            AccountServiceImpl().sendPasswordResetEmail(auth, uiState.value.email) { task ->
                uiState.value = uiState.value.copy(isLoading = false)
                if (task.isSuccessful) {
                    onSuccess()
                    showSnackbar(snackbarHostState, "Password reset email sent")
                } else if (task.exception != null) {
                    showSnackbar(
                        snackbarHostState,
                        task.exception!!.message ?: "Failed to send email"
                    )
                } else {
                    showSnackbar(snackbarHostState, "Something went wrong")
                }
            }
        }
    }

    fun login(
        snackbarHostState: SnackbarHostState,
        navController: NavController,
        onSuccess: () -> Unit
    ) {
        if (uiState.value.isLoading) return
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            AccountServiceImpl().authenticateWithEmail(
                auth,
                uiState.value.email,
                uiState.value.password
            ) { task ->
                uiState.value = uiState.value.copy(isLoading = false)
                if (task.isSuccessful) {
                    if (auth.currentUser?.isEmailVerified == true)
                        onSuccess()
                    else if (auth.currentUser?.isEmailVerified == false) {
                        showSnackbar(snackbarHostState, "Please verify your email")
                        navController.navigate(
                            NavEmailVerificationScreen(
                                uiState.value.email,
                                uiState.value.password
                            )
                        )
                    }
                } else if (task.exception != null) {
                    showSnackbar(
                        snackbarHostState,
                        task.exception!!.message ?: "Failed to login"
                    )
                } else {
                    showSnackbar(snackbarHostState, "Something went wrong")
                }
            }
        }
    }

    fun googleLogIn(context: Context, snackbarHostState: SnackbarHostState, onSuccess: () -> Unit) {
        uiState.value = uiState.value.copy(isLoading = true)
        val credentialManager = CredentialManager.create(context)
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setNonce(generateRandomNonce())
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(/* credentialOption = */ googleIdOption)
            .build()

        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                val credential = result.credential

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                val googleIdToken = googleIdTokenCredential.idToken

                val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                AccountServiceImpl().googleSignIn(auth, authCredential) { task ->
                    uiState.value = uiState.value.copy(isLoading = false)
                    if (task.isSuccessful) {
                        onSuccess()
                    } else if (task.exception != null) {
                        showSnackbar(
                            snackbarHostState,
                            task.exception!!.message ?: "Failed to login"
                        )
                    } else {
                        showSnackbar(snackbarHostState, "Failed to login")
                    }
                }
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(isLoading = false)
                showSnackbar(snackbarHostState, e.message ?: "Failed to login")
            }
        }
    }

    fun showSnackbar(snackbarHostState: SnackbarHostState, message: String) {
        viewModelScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    private fun generateRandomNonce(): String {
        val ranNonce = UUID.randomUUID().toString()

        val bytes = ranNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }
        return hashedNonce
    }
}


class LoginViewModelFactory(val auth: FirebaseAuth) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java))
            return LoginViewModel(auth) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}