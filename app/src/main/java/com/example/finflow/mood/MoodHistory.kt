package com.example.finflow.mood

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodHistory(navController: NavController) {
    val viewModel: MoodViewModel = viewModel()
    var showWait by remember {
        mutableStateOf(true)
    }
    var labels by remember {
        mutableStateOf(emptyList<LabelEntity>())
    }
    var thoughts by remember {
        mutableStateOf("")
    }
    var change by remember {
        mutableStateOf("")
    }
    val scrollableState = rememberScrollState()
    LaunchedEffect(key1 = Unit) {
        viewModel.initDateDocuments(getDate())
        viewModel.getAllLabels().collect { newLabels ->
            labels = newLabels
        }
        showWait = false
    }
    var lastAvg by remember {
        mutableStateOf(0.0F)
    }
    LaunchedEffect(key1 = Unit) {
        lastAvg = viewModel.getLastAvg(getDate())
    }

    if (showWait) {
        Wait()
    } else {
        val selectedSet by remember {
            mutableStateOf(mutableListOf<String>())
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Track Mood") }
                )
            }
        ) {
            val context = LocalContext.current
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
            ) {
                item {
                    Text(
                        text = "Question 1",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Text(
                        text = "Select your mood",
                        style = MaterialTheme.typography.headlineSmall, modifier =
                        Modifier.padding(horizontal = 16.dp)
                    )
                }
                items(labels.size) { index ->
                    var selected by remember {
                        mutableStateOf((labels[index].name in selectedSet))
                    }
                    FilterChip(
                        selected = selected,
                        onClick = {
                            if (selected)
                                selectedSet.remove(labels[index].name)
                            else
                                selectedSet.add(labels[index].name)
                            selected = !selected
                            Log.i(
                                "MoodHistory",
                                "All: $selectedSet Selected: ${labels[index].name}\n ${(labels[index].name in selectedSet)}"
                            )
                        },
                        label = { Text(labels[index].name) },
                        leadingIcon = {
                            if (selected)
                                Icon(Icons.Filled.Done, contentDescription = null)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
                item {

                    Text(
                        text = "Question 2",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Text(
                        text = "Share your thoughts",
                        style = MaterialTheme.typography.headlineSmall, modifier =
                        Modifier.padding(horizontal = 16.dp)
                    )
                }
                item {
                    OutlinedTextField(value = thoughts, onValueChange = {
                        thoughts = it
                    }, label = { Text("Thoughts...") },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
                item {
                    Text(
                        text = "Question 3",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Text(
                        text = "What changed your mood? Last Mood Intensity: $lastAvg",
                        style = MaterialTheme.typography.headlineSmall, modifier =
                        Modifier.padding(horizontal = 16.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.padding(16.dp))
                }
                item {
                    OutlinedTextField(value = change, onValueChange = {
                        change = it
                    }, label = { Text("Change") },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.padding(16.dp))
                }
                item {
                    Button(
                        onClick = {
                            val moodIntensity = calculateMoodIntensity(selectedSet, labels)
                            val moodHistory = MoodHistoryEntity(
                                id = getTime(),
                                avgMoodIntensity = moodIntensity,
                                thoughts = thoughts,
                                change = "",
                                date = getDate(),
                                time = getTime(),
                                moodLabels = labels.filter { it.name in selectedSet }
                                    .toMutableList()
                            )
                            viewModel.insertAMoodHistory(moodHistory)
//                            Calculations(context).creditCalculations(60,300.0F)
                            navController.navigate(MoodDestinations.success.route)
                        },
                    ) {
                        Text(text = "Submit")
                    }
                }
            }
        }
    }
}


@Composable
fun SuccessAfterSubmit() {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Success!!",
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}


fun calculateMoodIntensity(selectedSet: List<String>,labels: List<LabelEntity>): Float {
    var moodIntensity = 0.0F
    selectedSet.forEach {
        moodIntensity += (labels.find { label -> label.name == it }?.intensity?.toFloat() ?: 0.0F)
    }
    moodIntensity /= selectedSet.size
    return moodIntensity
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDate(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formatted = currentDateTime.format(formatter)
    return formatted?:"Default"
}

@RequiresApi(Build.VERSION_CODES.O)
fun getTime(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val formatted = currentDateTime.format(formatter)
    return formatted?:"Default"
}
