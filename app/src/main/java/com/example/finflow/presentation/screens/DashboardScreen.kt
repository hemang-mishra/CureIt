package com.example.finflow.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.finflow.R

@Preview(showBackground = true)
@Composable
fun DashboardScreen(){
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Row {
                    ProfilePictureView()
                }
            }
        }
    }
}


@Composable
fun ProfilePictureView(profilePictureUrl: String = "", size: Dp = 100.dp, onClick: () -> Unit={}){
    val context = LocalContext.current
    val brush = Brush.linearGradient(listOf( Color.Red, Color.Cyan, Color.Blue))
    Box(
        modifier = Modifier
            .width(size)
            .height(size)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .clickable {
                    onClick()
                }) {
            if (profilePictureUrl == "null" || profilePictureUrl.isEmpty()) {
                Icon(
                    imageVector = Icons.Default.Person,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
//                        .drawBehind {
//                            drawCircle(brush, style = Stroke(2F))
//                        }
                )

            } else {
                AsyncImage(
                    model = profilePictureUrl,
                    contentDescription = "profile",
                    imageLoader = ImageLoader(context),
                    placeholder = painterResource(id = R.drawable.user),
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
//                        .drawBehind {
//                            drawCircle(brush, style = Stroke(2F))
//                        }
                )
            }
        }
    }
}
