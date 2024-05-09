package com.example.finflow.mood

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.isDigitsOnly
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finflow.mood.ui.theme.FinFlowTheme

class MoodActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinFlowTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = MoodDestinations.moodHistory.route
                ) {
                    composable(MoodDestinations.labelList.route) {
                        LabelList(navController)
                    }
                    composable(MoodDestinations.addLabel.route) {
                        AddLabelBottomSheet(navController)
                    }
                    composable("${MoodDestinations.editLabel.route}/{labelId}") {
                        backStack ->
                        val labelId = backStack.arguments?.getString("labelId")
                        EditLabelSheet(if(labelId!= null && labelId.isDigitsOnly()) labelId.toInt() else 0, navController)
                    }
                    composable(MoodDestinations.moodHistory.route) {
                        MoodHistory(navController)
                    }
                    composable(MoodDestinations.success.route){
                        SuccessAfterSubmit()
                    }
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    FinFlowTheme {
    }
}