package com.example.finflow.presentation.login.emailverification

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.finflow.presentation.NavLoadingScreen
import com.example.finflow.presentation.login.createaccount.AppButton
import com.example.finflow.presentation.login.login.HeadingOfLoginScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun LogInSuccess(navController: NavController) {

    Log.i("MainActivity", "onCreate: ${FirebaseAuth.getInstance().currentUser?.isEmailVerified}")
    var imageVisible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        delay(500)
        imageVisible = true
    }
    Surface(
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
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        HeadingOfLoginScreen(largeText = "Yay! Login Successful", smallText = "")
                    }
                    item {
                        AnimatedVisibility(
                            visible = imageVisible,
                            enter = fadeIn() + scaleIn(), exit = fadeOut() + scaleOut()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.loginsuccesscenter),
                                contentDescription = null
                            )
                        }
                        if (!imageVisible)
                            Spacer(modifier = Modifier.height(300.dp))
                    }
                }
            }
            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                AppButton(
                    modifier = Modifier.width(314.dp),
                    text = "Next", isEnabled = true
                ) {
                    navController.navigate(NavLoadingScreen)
                }
                Spacer(modifier = Modifier.height(24.dp))
                BottomRowOfLoginSuccess()
                Spacer(modifier = Modifier.height(52.dp))
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
//@Preview(showBackground = true)
fun VerificationSuccess(navController: NavController) {
    var imageVisible by remember {
        mutableStateOf(false)
    }
    Log.i("MainActivity", "onCreate: ${FirebaseAuth.getInstance().currentUser?.isEmailVerified}")
    LaunchedEffect(key1 = Unit) {
        delay(200)
        imageVisible = true
    }
    Surface(
        modifier = Modifier
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
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        HeadingOfLoginScreen(largeText = "Verification Success", smallText = "")
                    }
                    item {
                        AnimatedVisibility(
                            visible = imageVisible,
                            enter = fadeIn() + scaleIn(), exit = fadeOut() + scaleOut()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.verificationsuccesscenter),
                                contentDescription = null
                            )
                        }
                        if (!imageVisible)
                            Spacer(modifier = Modifier.height(300.dp))
                    }
                    item {
                        Spacer(modifier = Modifier.height(60.dp))
                        AppButton(
                            modifier = Modifier.width(314.dp),
                            text = "Next", isEnabled = true
                        ) {
                            //Continue here with logic
                            navController.navigate(NavLoadingScreen)
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        BottomRowOfLoginSuccess()
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
fun BottomRowOfLoginSuccess() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.Start),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = "Need help ",
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFF454547),
                textAlign = TextAlign.Center,
            )
        )
        Text(
            text = "FAQ",
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFF454547),
                textAlign = TextAlign.Center,
            )
        )
        Text(
            text = "Term of use ",
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFF454547),
                textAlign = TextAlign.Center,
            )
        )
    }
}