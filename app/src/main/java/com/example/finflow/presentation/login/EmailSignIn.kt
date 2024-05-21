package com.example.finflow.presentation.login

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MarkEmailUnread
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finflow.R
import com.example.finflow.presentation.MainActivity
import com.example.finflow.presentation.destinations.Destinations
import com.example.finflow.presentation.signInToDashboard

/**
 * SignInUsingEmail is a composable function that provides the UI for the sign-in process using email.
 * It includes fields for entering email and password, and buttons for signing in and switching to sign-up mode.
 *
 * @param navController The NavController used for navigation.
 */
@Composable
fun SignInUsingEmail(navController: NavController) {
    var isSignInMode by remember {
        mutableStateOf(true)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var confirmPassword by remember {
        mutableStateOf("")
    }
    var passwordVisibility by remember {
        mutableStateOf(false)
    }
    var passwordError by remember {
        mutableStateOf<String?>(null)
    }
    val emailAuthentication = EmailAuthentication()
    val scrollView = rememberScrollState()
    @Composable
    fun SignIn() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .width(300.dp)
                .verticalScroll(scrollView)
        ) {
            Text(text = "Welcome Back!", style = MaterialTheme.typography.headlineLarge)
            Text(text= "Sign In", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(value = email, onValueChange = {
                email = it
                passwordError = null
            }, placeholder = { Text("Enter Email") })
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = password, onValueChange = {
                password = it
                passwordError = null
            },
                isError = passwordError != null,
                supportingText = {
                    if(passwordError != null)
                        Text(text = passwordError!!)
                },
                placeholder = { Text("Enter Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                })
            Spacer(modifier = Modifier.height(16.dp))
            Button(enabled = !isLoading,onClick = {
                //Implement password and email validity here
                if(!emailAuthentication.isValidEmail(email))
                {
                    Toast.makeText(context, "Invalid Email Format", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                passwordError = emailAuthentication.validatePassword(password)
                if(passwordError != null){
                    Toast.makeText(context, passwordError!!, Toast.LENGTH_SHORT).show()
                    return@Button
                }
                isLoading = true
                EmailAuthentication().signInWithEmail(email, password, context,
                    onSuccess = {
                        onSignInSuccessful(navController)
                        isLoading = false
                    },
                    onFailure = {
                        isLoading = false
                    })
            },
                modifier = Modifier.fillMaxWidth()) {
                Text(text = "Sign In")
            }
            Spacer(Modifier.height(8.dp))
            TextButton(enabled = !isLoading,
                onClick = {
                    if(emailAuthentication.isValidEmail(email)) {
                        isLoading = true
                        emailAuthentication.forgotPassword(email = email,
                            onSuccessfullySend = {
                                navController.navigate(Destinations.ForgotPassword.route)
                                isLoading = false
                            },
                            onFailed = {
                                Toast.makeText(context, "Could not send email", Toast.LENGTH_SHORT)
                                    .show()
                                isLoading = false
                            })
                    }
                    else{
                        Toast.makeText(context,"Invalid Email Format", Toast.LENGTH_SHORT).show()
                    }
                }) {
                Text("Forgot Password?")
            }
            Spacer(Modifier.height(8.dp))
            DividerWithText("or")
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = {
                isSignInMode = false
            }) {
                Text("Create New Account")
            }
        }
    }
    @Composable
    fun SignUp(){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .width(300.dp)
                .verticalScroll(scrollView)
        ) {
            Text(text = "New User?", style = MaterialTheme.typography.headlineLarge)
            Text(text= "Sign Up", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(value = email, onValueChange = {
                email = it
                passwordError = null
            }, placeholder = { Text("Enter Email") })
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = password, onValueChange = {
                password = it
                passwordError = null
            }, placeholder = { Text("Create Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                })
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it
                    passwordError = null},
                placeholder = { Text("Confirm Password") },
                isError = passwordError != null,
                supportingText = {
                    if(passwordError != null)
                        Text(text = passwordError!!)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                passwordError = emailAuthentication.validatePassword(password)
                if (!emailAuthentication.isValidEmail(email))
                    Toast.makeText(context, "Invalid Email Format", Toast.LENGTH_SHORT).show()
                else if (passwordError != null)
                    Toast.makeText(context, passwordError!!, Toast.LENGTH_SHORT).show()
                else if(password != confirmPassword){
                    passwordError = "Passwords do not match"
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
                else {
                    isLoading = true
                    EmailAuthentication().signUpWithEmail(email, password, context,
                        onSuccess = {
                            isLoading = false
                            onSignInSuccessful(navController)
                        },
                        onFailure = {
                            isLoading = false
                        })
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Sign Up")
            }
        }

    }
    Surface(
        color = if(!isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f),
            contentAlignment = Alignment.Center
        ) {
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
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
            ) {
                if (isLoading)
                    Wait()
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    if(isSignInMode)
                        SignIn()
                    else
                        SignUp()
                }
            }
        }
    }
}

/**
 * ForgotPassword is a composable function that provides the UI for the password reset process.
 * It includes a message indicating that a password reset link has been sent, and a button for going back to the sign-in screen.
 *
 * @param navController The NavController used for navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPassword(navController: NavController){
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Reset Password", onTextLayout = null) }, navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            })
        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            CircleImage(size = 80, imageVector = Icons.Default.MarkEmailUnread)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Password reset link has been sent on mail")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Please check your mail")
            Spacer(modifier = Modifier.height(32.dp))
            TextButton(onClick = {
                navController.popBackStack()
            }) {
                Text(text = "Go back to Sign In")
            }
        }
    }
}


/**
 * CircleImage is a composable function that displays an image in a circular shape.
 *
 * @param painterId The resource ID of the image to be displayed. This is optional and defaults to null.
 * @param imageVector The vector image to be displayed. This is optional and defaults to null.
 * @param size The size of the image in dp.
 * @param onClick A function to be invoked when the image is clicked. This is optional and defaults to an empty function.
 */
@Composable
fun CircleImage(
    painterId: Int? = null,
    imageVector: ImageVector? = null,
    size: Int,
    onClick: () -> Unit = {}
){
    Box(contentAlignment =
    Alignment.Center,modifier = Modifier
        .size(size.dp)
        .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
        .clickable(onClick = onClick)
    ) {
        if(painterId != null)
            Image(
                painter = painterResource(painterId),
                contentDescription = null,
                modifier = Modifier
                    .height((size - 8).dp)
                    .width((size - 8).dp)
                    .clip(CircleShape)
            )
        else if(imageVector != null){
            Image(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier
                    .height((size - 8).dp)
                    .width((size - 8).dp)
                    .clip(CircleShape),

                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            )
        }
    }
}

fun Context.getActivity(): Activity?= when(this){
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}


/**
 * Wait is a composable function that displays a linear progress indicator.
 */
@Composable
fun Wait(){
    LinearProgressIndicator(modifier = Modifier.fillMaxWidth()
    )
}


/**
 * isValidPhoneNumber is a function that checks if a phone number is valid.
 *
 * @param phoneNumber The phone number to be checked.
 * @return A boolean indicating whether the phone number is valid.
 */
fun isValidPhoneNumber(phoneNumber: String): Boolean {
    val regex = "^\\+91[0-9]{10}$".toRegex()
    return regex.matches(phoneNumber)
}

@Composable
fun DividerWithText(text: String = "Or"){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = MaterialTheme.colorScheme.outline
        )
    }
}

fun onSignInSuccessful(navController: NavController){
    navController.navigate(Destinations.MainScreen.route)
}