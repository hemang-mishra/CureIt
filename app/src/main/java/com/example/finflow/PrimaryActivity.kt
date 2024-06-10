package com.example.finflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.finflow.presentation.LoadingScreen
import com.example.finflow.presentation.NavLoadingScreen
import com.example.finflow.presentation.destinations.NavCreateAccountEmailScreen
import com.example.finflow.presentation.destinations.NavCreateAccountPasswordScreen
import com.example.finflow.presentation.destinations.NavEmailVerificationScreen
import com.example.finflow.presentation.destinations.NavForgotPassword
import com.example.finflow.presentation.destinations.NavLoginScreen
import com.example.finflow.presentation.destinations.NavLoginSuccessScreen
import com.example.finflow.presentation.destinations.NavVerificationSuccessScreen
import com.example.finflow.presentation.login.createaccount.CreateAccountEmailScreen
import com.example.finflow.presentation.login.createaccount.CreateAccountPasswordScreen
import com.example.finflow.presentation.login.createaccount.CreateAccountViewModel
import com.example.finflow.presentation.login.createaccount.CreateAccountViewModelFactory
import com.example.finflow.presentation.login.emailverification.EmailVerificationViewModel
import com.example.finflow.presentation.login.emailverification.EmailVerificationViewModelFactory
import com.example.finflow.presentation.login.emailverification.ForgotPassword
import com.example.finflow.presentation.login.emailverification.LogInSuccess
import com.example.finflow.presentation.login.emailverification.VerificationSuccess
import com.example.finflow.presentation.login.emailverification.VerifyEmail
import com.example.finflow.presentation.login.login.LogInScreen
import com.example.finflow.presentation.login.login.LoginViewModel
import com.example.finflow.presentation.login.login.LoginViewModelFactory
import com.example.finflow.ui.theme.FinFlowTheme
import com.google.firebase.auth.FirebaseAuth

class PrimaryActivity : ComponentActivity() {
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinFlowTheme {
                val navController = rememberNavController()

                val loginViewModel: LoginViewModel =
                    viewModel(factory = LoginViewModelFactory(auth))
                val createAccountViewModel: CreateAccountViewModel =
                    viewModel(factory = CreateAccountViewModelFactory(auth))
                val emailVerificationViewModel: EmailVerificationViewModel =
                    viewModel(factory = EmailVerificationViewModelFactory(auth))
                NavHost(
                    navController = navController,
                    startDestination = if (FirebaseAuth.getInstance().currentUser != null)
                        NavLoadingScreen
                    else NavLoginScreen
                ) {
                    composable<NavCreateAccountEmailScreen> {
                        CreateAccountEmailScreen(
                            viewModel = createAccountViewModel,
                            navController = navController
                        )
                    }
                    composable<NavCreateAccountPasswordScreen> {
                        CreateAccountPasswordScreen(
                            viewModel = createAccountViewModel,
                            navController = navController
                        )
                    }
                    composable<NavLoginScreen>(
                        enterTransition = {
                            slideInHorizontally(animationSpec = tween(300),
                                initialOffsetX = { fullWidth -> fullWidth }
                            )
                        },
                    ) {
                        LogInScreen(loginViewModel, navController)
                    }
                    composable<NavVerificationSuccessScreen> {
                        VerificationSuccess(navController)
                    }
                    composable<NavLoginSuccessScreen> {
                        LogInSuccess(navController)
                    }
                    composable<NavEmailVerificationScreen> {
                        val args = it.toRoute<NavEmailVerificationScreen>()
                        VerifyEmail(
                            emailVerificationViewModel,
                            navController,
                            args.email,
                            args.password
                        )
                    }
                    composable<NavForgotPassword> {
                        ForgotPassword(navController = navController)
                    }
                    composable<NavLoadingScreen> {
//                        loadingViewModel.fetchData()
//                        LaunchedEffect(key1 = loadingViewModel.isLoadingComplete.value) {
//                            if(loadingViewModel.isLoadingComplete.value)
//                                loadingViewModel.onCompleterFetch(navController)
//                        }
                        LoadingScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting4(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview5() {
    FinFlowTheme {
        Greeting4("Android")
    }
}