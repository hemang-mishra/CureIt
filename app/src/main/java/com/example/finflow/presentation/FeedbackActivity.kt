package com.example.finflow.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finflow.presentation.destinations.Destinations
import com.example.finflow.domain.usecases.FeedbackEntity
import com.example.finflow.domain.usecases.FeedbackRepo
import com.example.finflow.debitAppLogic.Calculations
import com.example.finflow.debitAppLogic.Logic
import com.example.finflow.ui.theme.FinFlowTheme

class FeedbackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val label = intent.getStringExtra("label") ?: "Default"
        setContent {
            FinFlowTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainUI(label)
                }
            }
        }
    }

}


@Composable
fun MainUI(label: String){
    var progress : MutableState<Float> = remember {
        mutableFloatStateOf(0.5f)
    }
    var selectedStateIndex : MutableState<Int> = remember {
        mutableIntStateOf(0)
    }
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(progress = progress.value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp))
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Destinations.attentionList.toString()) {
            composable(Destinations.attentionList.toString()) {
                AttentionStateForm(navController, selectedStateIndex)
            }
            composable(Destinations.feedbackForm.toString()) {
                FeedbackForm(label,navController, progress, selectedStateIndex)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttentionStateForm(navController: NavController, selectedStateIndex : MutableState<Int> ){
    var selected by remember {
        mutableIntStateOf(-1)
    }
    val attentionStates = AttentionStates().list

    LazyColumn {
        item {
            Text(text = "Question 1 of 2",
                modifier = Modifier.padding(16.dp,16.dp,16.dp,0.dp))
            Text(text = "Please share your attention state!",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp,0.dp,16.dp,16.dp))
        }
        items(attentionStates.size) { index ->
            Card(onClick = {
                           selected = index
            },
                colors = CardDefaults.cardColors(
                    containerColor = if(selected == index) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if(selected == index) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)) {
                Row(modifier = Modifier.padding(8.dp)) {
                    RadioButton(selected = (selected == index), onClick = {
                        selectedStateIndex.value = index
                        navController.navigate(Destinations.feedbackForm.toString())
                    })
                    Column {
                        Text(text = attentionStates[index].title,
                            style = MaterialTheme.typography.titleMedium)
                        Text(text = attentionStates[index].description)
                    }
                }
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackForm(label: String,navController: NavController,
                 progress: MutableState<Float>,
                 selectedStateIndex : MutableState<Int>
) {
    progress.value = 1.0f
    var desription by remember { mutableStateOf("") }
    var importance by remember { mutableStateOf(0.0f) }
    var selected by remember { mutableIntStateOf(5) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
    ) {

        Text(text = "Question 2 of 2")
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Please share your feedback!", style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(onClick = { /*TODO*/ },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
            ,
            modifier =
            Modifier
                .fillMaxWidth()){
            Text(text = "Did you have the right sanskaras?",
                modifier = Modifier.padding(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {  selected = 1 }
            ) {
                RadioButton(selected = (selected == 1), onClick = { selected = 1 },
                )
                Text(text = "Yes")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {  selected = 0 }
            ) {
                RadioButton(selected = (selected == 0), onClick = { selected = 0 },
                )
                Text(text = "No")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Text(
                text = "How can you improve?", modifier = Modifier.padding(16.dp)
            )
            OutlinedTextField(value = desription, onValueChange = {
                desription = it
            }, label = { Text("Feedback") }, modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Text(text ="Weight of feedback out of 10: ${(importance*10).toInt()}", modifier = Modifier.padding(16.dp))
            Slider(value = importance.toFloat(), onValueChange = {
                importance = it
            },
                modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp))
//            OutlinedTextField(
//                value = importance.toString(),
//                onValueChange = {
//                    importance = it
//                },
//                label = { Text("Weight of feedback out of 10") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        val desription2 = "Selected State was ${AttentionStates().list[selectedStateIndex.value].title} and the feedback was $desription"
        Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val date = Logic().currentDateAndTime()
                val repo = FeedbackRepo(label)
                repo.insertFeedback(
                    FeedbackEntity(date, 0, desription2, importance.toInt(), label)
                )
                val intent = Intent(context, StartActivity::class.java)
                val balance = Calculations(context).getBalanceInSharedPref()
                Calculations(context).saveNewBalanceInSharedPref(balance + 2000000 * importance.toFloat())
                context.startActivity(intent)
            },
                modifier = Modifier.align(Alignment.CenterHorizontally)){
                Text("Submit")
            }

    }
}

data class AttentionStates(val list: List<AttentionStateEntity> =
    listOf(
        AttentionStateEntity(1, "Mudha", "Slugglish, low motivation and dull"),
        AttentionStateEntity(2, "Kshipta", "Scattered and Restless mind!"),
        AttentionStateEntity(3, "Vikshipta", "Phases of attention and distraction"),
        AttentionStateEntity(4, "Ekagra", "One pointed concentration"),
        AttentionStateEntity(5, "Nirodha", "Highest state of attention and concentration")
    )
)

data class AttentionStateEntity(val id: Int,
    val title: String,
    val description: String
    )

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    FinFlowTheme {
        MainUI(label ="deg")
    }
}