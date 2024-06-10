package com.example.finflow.presentation.login.emailverification

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finflow.R
import com.example.finflow.presentation.login.createaccount.AppButton
import com.example.finflow.presentation.login.login.HeadingOfLoginScreen


@Composable
fun VerifyEmail(
    viewModel: EmailVerificationViewModel, navController: NavController,
    email: String, password: String
) {
    val uiState by viewModel.uiState
    val imageVisible by viewModel.isImageVisible
    viewModel.reloadImage()
    val snackbarHostState = remember { SnackbarHostState() }
    if (uiState == EmailVerificationState.NOT_INITIALIZED) {
        viewModel.sendVerificationEmail(snackbarHostState)
    }
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
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item {
                        if (uiState == EmailVerificationState.SENDING_MAIL || uiState == EmailVerificationState.LOGGING_IN)
                            Wait()
                        Spacer(modifier = Modifier.height(24.dp))
                        HeadingOfLoginScreen(
                            largeText = uiState.uiElement.largeHeadingText,
                            smallText = uiState.uiElement.smallHeadingText,
                        )
                        Spacer(
                            modifier = Modifier
                                .height(60.dp)
                                .padding(paddingValues)
                        )
                    }
                    item {
                        AnimatedVisibility(
                            visible = imageVisible,
                            enter = fadeIn() + scaleIn(), exit = fadeOut() + scaleOut()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.verificationemailcenter),
                                contentDescription = null
                            )
                        }
                        if (!imageVisible)
                            Spacer(modifier = Modifier.height(250.dp))
                    }
                    item {
                        Spacer(modifier = Modifier.height(36.dp))
                        AppButton(
                            isEnabled = uiState.uiElement.isButtonEnabled,
                            text = uiState.uiElement.buttonText,
                        ) {
                            viewModel.buttonClicked(
                                email,
                                password,
                                navController,
                                snackbarHostState
                            )
                        }
                    }
                    item {
                        BottomResendEmailRow(uiState = uiState, onClick = {
                            viewModel.sendVerificationEmail(snackbarHostState)
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
fun BottomResendEmailRow(uiState: EmailVerificationState, onClick: () -> Unit) {
    if (uiState == EmailVerificationState.SENT_MAIL) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "Problem with verification? ",
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF454547),
                    textAlign = TextAlign.Center,
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Resend Email",

                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFB8A7A),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.clickable {
                    onClick()
                }
            )
        }
    }
}

@Composable
fun Wait() {
    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth()
    )
}
