package com.example.finflow.presentation.login.emailverification

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.finflow.domain.AccountServiceImpl
import com.example.finflow.presentation.destinations.NavLoginSuccessScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EmailVerificationViewModel(val auth: FirebaseAuth) : ViewModel() {
    var isImageVisible = mutableStateOf(true)
        private set
    var uiState = mutableStateOf(EmailVerificationState.NOT_INITIALIZED)
        private set

    fun sendVerificationEmail(snackbarHostState: SnackbarHostState) {
        val email = auth.currentUser?.email
        if (email == null) {
            uiState.value = EmailVerificationState.FAILED_TO_SEND_MAIL
            Log.e("EmailVerificationViewModel", "Email is null")
            return
        }
        uiState.value = EmailVerificationState.SENDING_MAIL
        viewModelScope.launch {
            AccountServiceImpl().sendEmailVerificationLink(auth, email) { task ->
                if (task.isSuccessful) {
                    uiState.value = EmailVerificationState.SENT_MAIL
                    reloadImage()
                } else if (task.exception != null) {
                    reloadImage()
                    uiState.value = EmailVerificationState.FAILED_TO_SEND_MAIL
                    showSnackbar(
                        snackbarHostState,
                        task.exception!!.message ?: "Failed to send email"
                    )
                    Log.e("EmailVerificationViewModel", "Failed to send email", task.exception)
                } else {
                    reloadImage()
                    uiState.value = EmailVerificationState.FAILED_TO_SEND_MAIL
                    showSnackbar(snackbarHostState, "Failed to send email")
                    Log.e("EmailVerificationViewModel", "Failed to send email")
                }
            }
        }
    }

    fun showSnackbar(snackbarHostState: SnackbarHostState, message: String) {
        viewModelScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    fun reloadImage() {
        isImageVisible.value = false
        viewModelScope.launch {
            delay(500)
            isImageVisible.value = true
        }
    }

    private fun reAuthenticateUser(
        email: String,
        password: String,
        snackbarHostState: SnackbarHostState,
        onSuccessful: () -> Unit
    ) {
        val email = auth.currentUser?.email
        if (email == null) {
            uiState.value = EmailVerificationState.FAILED_TO_SEND_MAIL
            Log.e("EmailVerificationViewModel", "Email is null")
            return
        }
        viewModelScope.launch {
            AccountServiceImpl().reauthenticateAfterEmailVerification(
                auth,
                email = email,
                password = password
            ) { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser?.isEmailVerified == false) {
                        uiState.value = EmailVerificationState.SENT_MAIL
                        showSnackbar(snackbarHostState, "Email is not verified")
                        Log.e("EmailVerificationViewModel", "Email is not verified")
                        return@reauthenticateAfterEmailVerification
                    }
                    onSuccessful()
                } else if (task.exception != null) {
                    uiState.value = EmailVerificationState.SENT_MAIL
                    reloadImage()
                    showSnackbar(snackbarHostState, task.exception!!.message ?: "Failed to login")
                    Log.e("EmailVerificationViewModel", "Failed to login", task.exception)
                } else {
                    uiState.value = EmailVerificationState.SENT_MAIL
                    reloadImage()
                    showSnackbar(snackbarHostState, "Failed to login")
                    Log.e("EmailVerificationViewModel", "Failed to login")
                }
            }
        }

    }

    fun buttonClicked(
        email: String,
        password: String,
        navController: NavController,
        snackbarHostState: SnackbarHostState
    ) {
        when (uiState.value) {
            EmailVerificationState.FAILED_TO_SEND_MAIL -> {
                sendVerificationEmail(snackbarHostState)
            }

            EmailVerificationState.SENT_MAIL -> {
                uiState.value = EmailVerificationState.LOGGING_IN
                reAuthenticateUser(
                    email = email,
                    password = password,
                    snackbarHostState = snackbarHostState
                ) {
                    navController.navigate(NavLoginSuccessScreen)
                }
            }

            else -> {
            }
        }
    }
}

enum class EmailVerificationState(val uiElement: EmailVerificationUIElements) {
    SENDING_MAIL(
        EmailVerificationUIElements(
            largeHeadingText = "Sending Verification Email",
            smallHeadingText = "Please wait for a while!",
            buttonText = "Sending....",
            isScreenLoading = true,
            isButtonEnabled = false
        )
    ),
    FAILED_TO_SEND_MAIL(
        EmailVerificationUIElements(
            largeHeadingText = "Failed to send email",
            smallHeadingText = "Please try again!",
            buttonText = "Try Again",
            isScreenLoading = false,
            isButtonEnabled = true
        )
    ),
    SENT_MAIL(
        EmailVerificationUIElements(
            largeHeadingText = "Email Sent",
            smallHeadingText = "Login after verifying your email!",
            buttonText = "Log In",
            isScreenLoading = false,
            isButtonEnabled = true
        )
    ),
    LOGGING_IN(
        EmailVerificationUIElements(
            largeHeadingText = "Logging In",
            smallHeadingText = "Please wait for a while!",
            buttonText = "Logging In....",
            isScreenLoading = true,
            isButtonEnabled = false
        )
    ),
    NOT_INITIALIZED(
        EmailVerificationUIElements(
            largeHeadingText = "Not Initialized",
            smallHeadingText = "Please wait for a while!",
            buttonText = "Not Initialized",
            isScreenLoading = true,
            isButtonEnabled = false
        )
    )
}

data class EmailVerificationUIElements(
    var largeHeadingText: String,
    val smallHeadingText: String,
    val buttonText: String,
    val isScreenLoading: Boolean,
    val isButtonEnabled: Boolean
)

class EmailVerificationViewModelFactory(val auth: FirebaseAuth) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmailVerificationViewModel::class.java))
            return EmailVerificationViewModel(auth = auth) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}