package com.example.finflow.presentation.login.createaccount

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
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finflow.R
import com.example.finflow.presentation.destinations.NavCreateAccountPasswordScreen

@Composable
fun CreateAccountEmailScreen(viewModel: CreateAccountViewModel, navController: NavController) {
    val uiState by viewModel.uiState
    if (!uiState.isEmailValid) {
        emailCheck(uiState.email, viewModel::onEmailErrorStateChange)
    }
    Scaffold(
    ) {
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    item {
                        HeadingOfCreateAccount(
                            modifier = Modifier
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(40.dp))
                        Image(
                            imageVector = Icons.Default.Done,
                            contentDescription = "done",
                            modifier = Modifier
                                .requiredSize(size = 222.dp)
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.width(8.dp))
                        AppTextField(
                            modifier = Modifier,
                            value = uiState.email,
                            onValueChange = viewModel::onEmailChange,
                            isError = !uiState.isEmailValid,
                            errorText = uiState.emailError,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        AppButton(
                            modifier = Modifier.width(314.dp),
                            isEnabled = true
                        ) {
                            if (emailCheck(uiState.email, viewModel::onEmailErrorStateChange)) {
                                return@AppButton
                            }
                            navController.navigate(NavCreateAccountPasswordScreen)
                        }
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
fun AppButton(
    modifier: Modifier = Modifier,
    text: String = "Continue",
    isEnabled: Boolean,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clickable {
                if (isEnabled) onClick()
            }

    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .shadow(
                    elevation = 4.dp,
                    spotColor = Color.Black,
                    ambientColor = Color.Black,
                    shape = RoundedCornerShape(15.dp)
                )
                .shadow(
                    elevation = 14.dp,
                    spotColor = Color(0x40050505),
                    ambientColor = Color(0x40000000)
                )
                .fillMaxWidth(0.9f)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(
                    color = if (isEnabled) Color(0xfffb8a7a) else
                        Color(0xFFF3AEA5).copy(alpha = 1.0f)
                )
                .padding(16.dp)
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight(600),
                    color = Color(0xFFEEF6F8),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.32.sp,
                )
            )
        }
    }
}

@Composable
fun HeadingOfCreateAccount(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = "Create Your Account",

            // heading large 24
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 48.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF454547),
            )
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Create account for planning your life",

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
fun AppTextField(
    modifier: Modifier = Modifier, value: String, onValueChange: (String) -> Unit,
    outerText: String = "Login with email",
    placeholderText: String = "Enter your email",
    icon: Painter = painterResource(id = R.drawable.baseline_email_24),
    isError: Boolean = false,
    errorText: String = "Invalid Email Format",
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {

    Box(
        modifier = modifier.width(314.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = outerText,
                // content 16 bold
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF454547),
                ),
                modifier = Modifier.height(20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LoginTextField(
                modifier =
                Modifier.fillMaxWidth(),
                placeholderText = placeholderText,
                value = value,
                icon = icon,
                isError = isError,
                errorText = errorText,
                keyboardOptions = keyboardOptions,
                isPassword = isPassword
            ) {
                onValueChange(it)
            }
        }
    }
}


@Composable
fun LoginTextField(
    modifier: Modifier, placeholderText: String,
    icon: Painter = painterResource(id = R.drawable.baseline_email_24),
    value: String, isError: Boolean,
    isPassword: Boolean, keyboardOptions: KeyboardOptions,
    errorText: String, onValueChange: (String) -> Unit,
) {
    var showPassword by remember {
        mutableStateOf(false)
    }
    OutlinedTextField(value = value, onValueChange = {
        onValueChange(it)
    }, placeholder = {
        Text(
            text = placeholderText,
            // content 16
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight(400),
            )
        )
    },
        keyboardOptions = keyboardOptions,
        visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,
        isError = isError,
        supportingText = { if (isError) Text(text = errorText) },
        leadingIcon = {
            Icon(
                painter = icon, contentDescription = null,
                modifier = Modifier
                    .width(20.dp)
                    .height(16.dp)
            )

        },
        trailingIcon = {
            if (isPassword)
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        painter = painterResource(
                            id = if (showPassword) R.drawable.baseline_visibility_off_24
                            else
                                R.drawable.baseline_visibility_24
                        ),
                        tint = Color(0.494f, 0.482f, 0.482f, 0.502f), contentDescription = null
                    )
                }
        },
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    )
}