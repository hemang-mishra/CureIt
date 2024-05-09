package com.example.finflow.mood

import android.graphics.Paint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import com.example.finflow.presentation.destinations.Destinations
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun LabelList(navController: NavController){
    val viewModel: MoodViewModel = viewModel()
    var showWait by remember {
        mutableStateOf(true)
    }
    var labels by remember {
        mutableStateOf(emptyList<LabelEntity>())
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllLabels().collect{
            newLabels -> labels = newLabels
        }
        showWait = false
    }
    if(showWait){
        Wait()
    } else {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(
                        "Labels", style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                })
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    navController.navigate(MoodDestinations.addLabel.route)
                }) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                }
            }
        ) {
            LazyColumn(
                contentPadding = it
            ) {
                items(labels.size) {
                    LabelCard(labels[it], -1, navController)
                }
            }
        }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelCard(label: LabelEntity = LabelEntity(name = "Study"),
              selectedIn: Int = 0,
              navController: NavController) {
    var selected by remember {
        mutableIntStateOf(selectedIn)
    }
    OutlinedCard(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                navController.navigate("${MoodDestinations.editLabel.route}/${label.id}")
                selected = label.id
            }
        ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                if (label.id == selected) Icons.Filled.ArrowForward
                else Icons.Outlined.ArrowForward, contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = label.name, style = MaterialTheme.typography.bodyLarge)
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                Box {
                    Column(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                        Text(
                            text = label.intensity.toString(),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddLabelBottomSheet(navController: NavController) {
        val viewModel : MoodViewModel = viewModel()
        var labels by remember {
            mutableStateOf(emptyList<LabelEntity>())
        }
        LaunchedEffect(key1 = Unit) {
            viewModel.getAllLabels().collect{
                newLabels -> labels = newLabels
            }
        }
        var name by remember {
            mutableStateOf("")
        }
        var intensity by remember {
            mutableIntStateOf(2)
        }
        var freq by remember {
            mutableStateOf("0")
        }
        ModalBottomSheet(onDismissRequest = {
            navController.navigate(MoodDestinations.labelList.route)
        }) {
            Text(text = "Add Label", style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp))
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)){
                Spacer(Modifier.padding(16.dp))
                OutlinedTextField(value = name,label = {
                    Text("Name")
                }, onValueChange = { name = it },
                    modifier = Modifier.padding(16.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Choose Intensity: $intensity",
                    modifier = Modifier.padding(16.dp))
                Slider(value = intensity / 10.0f,
                    modifier = Modifier.padding(16.dp), onValueChange = { intensity = (it * 10).toInt() })
                OutlinedTextField(
                    value = freq.toString(),
                    label = {Text("Frequency")},
                    onValueChange = { freq = if (it.isDigitsOnly() && it.isNotEmpty()) it else "0"
                    },
                    modifier = Modifier.padding(16.dp))
                Spacer(Modifier.padding(16.dp))
            }
            Button(onClick = {
                             viewModel.insertALabel(LabelEntity(id = labels.size ,name = name, intensity = intensity, freq = freq.toInt()))
                navController.navigate(MoodDestinations.labelList.route)
            },
                modifier = Modifier.padding(16.dp)) {
                Text(text = "Save")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EditLabelSheet(labelIndex: Int,navController: NavController) {
        var recieved by remember {
            mutableStateOf(false)
        }
        val viewModel: MoodViewModel = viewModel()
        var labels by remember {
            mutableStateOf(emptyList<LabelEntity>())
        }
        LaunchedEffect(key1 = Unit) {
            viewModel.getAllLabels().collect{
                    newLabels -> labels = newLabels
            }
            recieved = true
        }
        if(recieved) {
            val label by remember {
                mutableStateOf(labels[labelIndex])
            }
            var name by remember {
                mutableStateOf(label.name)
            }
            var intensity by remember {
                mutableIntStateOf(label.intensity)
            }
            var freq by remember {
                mutableStateOf(label.freq.toString())
            }
            ModalBottomSheet(onDismissRequest = {
                navController.navigate(MoodDestinations.labelList.route)
            }) {
                Text(
                    text = "Edit Label", style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Spacer(Modifier.padding(16.dp))
                    OutlinedTextField(value = name, label = {
                        Text("Name")
                    }, onValueChange = { name = it },
                        modifier = Modifier.padding(16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Choose Intensity: $intensity",
                        modifier = Modifier.padding(16.dp)
                    )
                    Slider(value = intensity / 10.0f,
                        modifier = Modifier.padding(16.dp),
                        onValueChange = { intensity = (it * 10).toInt() })
                    OutlinedTextField(
                        value = freq.toString(),
                        label = { Text("Frequency") },
                        onValueChange = { freq = it },
                        modifier = Modifier.padding(16.dp)
                    )
                    Spacer(Modifier.padding(16.dp))
                }
                Button(
                    onClick = {
                        viewModel.updateALabel(LabelEntity(label.id, name, intensity, freq.toInt()))
                        navController.navigate(MoodDestinations.labelList.route)
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Save")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }else
            Wait()
    }


    @Composable
    fun Wait() {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

data class Sample(
    val labels : List<LabelEntity> = listOf(
        LabelEntity(0, "Happy", 10, 5),
        LabelEntity(0, "Distracted", 6, 5),
        LabelEntity(0, "Sad", 2, 5),
        LabelEntity(0, "Anxious", 3, 5),
    )
)