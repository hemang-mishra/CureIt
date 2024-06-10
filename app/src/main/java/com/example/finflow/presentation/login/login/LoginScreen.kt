package com.example.finflow.presentation.login.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finflow.R
import com.example.finflow.presentation.destinations.NavCreateAccountEmailScreen
import com.example.finflow.presentation.destinations.NavForgotPassword
import com.example.finflow.presentation.destinations.NavLoginSuccessScreen
import com.example.finflow.presentation.login.createaccount.AppButton
import com.example.finflow.presentation.login.createaccount.AppTextField
import com.example.finflow.presentation.login.createaccount.emailCheck
import com.example.finflow.presentation.login.createaccount.passwordCheck
import com.example.finflow.presentation.login.emailverification.Wait

@Composable
fun LogInScreen(viewModel: LoginViewModel, navController: NavController) {
    val uiState by viewModel.uiState
    if (!uiState.isEmailValid) {
        emailCheck(
            email = uiState.email, error = viewModel::onEmailErrorStateChange
        )
    }
    if (!uiState.isPasswordValid) {
        passwordCheck(password = uiState.password, error = viewModel::onPasswordErrorStateChange)
    }
    val passwordIcon = painterResource(id = R.drawable.baseline_password_24)
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth()
                    .fillMaxHeight(0.77f)
                    .clip(shape = RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp))
                    .background(color = Color(0xffeef6f8))
            ) {
                if (uiState.isLoading)
                    Wait()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item {
                        HeadingOfLoginScreen(
                            largeText = "Let’s get Started!",
                            smallText = "lets login for explore continues"
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                    item {
                        AppTextField(
                            modifier = Modifier,
                            value = uiState.email,
                            onValueChange = viewModel::onEmailChange,
                            outerText = "Login with email",
                            placeholderText = "Enter your email",
                            icon = painterResource(id = R.drawable.baseline_email_24),
                            isError = !uiState.isEmailValid,
                            errorText = uiState.emailError,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        AppTextField(
                            modifier = Modifier,
                            value = uiState.password,
                            onValueChange = viewModel::onPasswordChange,
                            outerText = "Password",
                            placeholderText = "Enter your password",
                            icon = passwordIcon,
                            isPassword = true,
                            isError = !uiState.isPasswordValid,
                            errorText = uiState.passwordError,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )
                        Text(
                            text = "Forgot password?",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 70.sp,
                                fontWeight = FontWeight(500),
                                color = Color(0xFF454547),
                                textAlign = TextAlign.Right,
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .width(314.dp)
                                .clickable {
                                    if (!emailCheck(
                                            email = uiState.email,
                                            error = viewModel::onEmailErrorStateChange
                                        )
                                    ) {
                                        viewModel.forgotPassword(snackbarHostState, navController) {
                                            navController.navigate(NavForgotPassword)
                                        }
                                    }
                                }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        AppButton(
                            modifier = Modifier.width(314.dp),
                            text = "Sign in",
                            isEnabled = !uiState.isLoading
                        ) {
                            if (emailCheck(
                                    email = uiState.email,
                                    error = viewModel::onEmailErrorStateChange
                                )
                                || passwordCheck(
                                    password = uiState.password,
                                    error = viewModel::onPasswordErrorStateChange
                                )
                            ) {
                                return@AppButton
                            }
                            viewModel.login(snackbarHostState, navController) {
                                navController.navigate(NavLoginSuccessScreen)
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        DividerWithText(
                            modifier = Modifier.width(314.dp),
                            text = "You can Connect with"
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = R.drawable.btnsigninwithgoogle),
                            contentDescription = "image description",
                            contentScale = ContentScale.None,
                            modifier = Modifier
                                .padding(0.dp)
                                .width(44.dp)
                                .height(44.00044.dp)
                                .clickable {
                                    viewModel.googleLogIn(context, snackbarHostState) {
                                        navController.navigate(NavLoginSuccessScreen)
                                    }
                                }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        BottomRowOfLoginScreen(onSignUpClick = {
                            navController.navigate(NavCreateAccountEmailScreen)
                        })
                    }
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.07f))
                Image(
                    painter = painterResource(id = R.drawable.bannericon),
                    contentDescription = "logo + name",
                    colorFilter = ColorFilter.tint(Color(0xffeef6f8)),
                    modifier = Modifier
                        .requiredWidth(width = 99.dp)
                        .requiredHeight(height = 84.dp)
                )
            }
        }
    }
}

@Composable
private fun BottomRowOfLoginScreen(onSignUpClick: () -> Unit) {
    Row {
        Text(
            text = "Don’t have an account?",
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFF454547),
                textAlign = TextAlign.Center,
            )
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Sign Up here",

            // smallest 12
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFFFB8A7A),
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.clickable { onSignUpClick() }
        )
    }
}

@Composable
fun HeadingOfLoginScreen(largeText: String, smallText: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = largeText,

            // heading large 24
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 48.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF454547),
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = smallText,

            // content 16
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF454547),
                textAlign = TextAlign.Center,
            )
        )
    }
}


@Composable
fun DividerWithText(modifier: Modifier, text: String = "Or") {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(2.dp),
            color = Color(0xFFADADAD)
        )
        Text(
            text = text,
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFFADADAD),
            ),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(2.dp),
            color = Color(0xFFADADAD)
        )
    }
}

/**
 * validatePassword is a function that validates a password based on certain criteria.
 *
 * @param password The password to be validated.
 * @return A string containing the validation error message if the password is invalid, or null if the password is valid.
 */
fun validatePassword(password: String): String? {
    if (password.length < 8) {
        return "Password should be at least 8 characters long"
    }
    if (!password.any { it.isUpperCase() }) {
        return "Password should contain at least one uppercase letter"
    }
    if (!password.any { it.isLowerCase() }) {
        return "Password should contain at least one lowercase letter"
    }
    if (!password.any { it.isDigit() }) {
        return "Password should contain at least one digit"
    }
    if (!password.contains(Regex("[@#$%^&+=]"))) {
        return "Password should contain at least one special character (@, #, $, %, ^, &, +, =)"
    }
    return null
}


/**
 * isValidEmail is a function that checks if an email is valid.
 *
 * @param email The email to be checked.
 * @return A boolean indicating whether the email is valid.
 */
fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    return email.matches(emailRegex.toRegex())
}