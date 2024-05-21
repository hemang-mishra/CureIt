package com.example.finflow.presentation.login

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finflow.R
import com.example.finflow.presentation.destinations.Destinations
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun SignInUI(
    navController: NavController
) {
    val scrollView = rememberScrollState()
    var isLoading by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    val auth = FirebaseAuth.getInstance()
    Surface(
        color = if(!isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
    ){
        Spacer(modifier = Modifier.height(32.dp))
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.25f),
            contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.bannericon),
                contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Card(shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                ,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
            ) {
                if(isLoading)
                    Wait()
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(300.dp)
                            .verticalScroll(scrollView)
                    ) {

                        Text(text = "CureIt Save your time!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(48.dp))
                        DividerWithText("Log in or sign up")
                        Spacer(modifier = Modifier.height(32.dp))
//                        DividerWithText(text = "or")
//                        Spacer(modifier = Modifier.height(32.dp))
                        Row {
                            GoogleSignInButton(onClickUI = {
                            },
                                onFailureUI = {
                                    isLoading = false
                                },
                                onSuccess = {
                                    onSignInSuccessful(navController)
                                    isLoading = false
                                })
                            Spacer(modifier = Modifier.width(48.dp))
                            CircleImage(imageVector = Icons.Default.Email, size = 40,
                                onClick = {
                                    if(!isLoading)
                                        navController.navigate(Destinations.EmailSignIn.route)
                                })

                        }
                    }
                }
            }
        }
    }
}