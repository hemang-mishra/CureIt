package com.example.finflow.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.finflow.goals.GoalEntity

@Composable
fun GainScreen(paddingValues: PaddingValues){
    LazyVerticalGrid(columns = GridCells.Adaptive(100.dp),
        modifier = Modifier.padding(paddingValues)) {
        items(100){
            GridCell()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GridCell(goalEntity: GoalEntity = GoalEntity(1, "Domain", 180.0f, 50.0f, 0, 90.0f)){
    ElevatedCard(onClick = { /*TODO*/ },
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .height(150.dp)) {
        Text(text = "Domain",
            style = MaterialTheme.typography.headlineSmall
        , modifier = Modifier.padding(8.dp))
        Row{
            Text(text = "Rate: ",
                modifier = Modifier.padding(8.dp,8.dp,0.dp,8.dp))
            Text(text = "180.0",
                modifier = Modifier.padding(0.dp,8.dp,8.dp,8.dp))
        }
        Text("50.0%  of  90.0%",
            modifier = Modifier.padding(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GainBottomSheet(goalEntity: GoalEntity,isNew: Boolean, onDismissRequest: () -> Unit,
                    onClickSave: () -> Unit){
    var name by rememberSaveable { mutableStateOf(goalEntity.domain) }
    var rate by rememberSaveable { mutableStateOf(goalEntity.rate.toString()) }

    ModalBottomSheet(onDismissRequest = { /*TODO*/ }) {
        Text(text = "Update or Add Gain",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp))
        Column {
            OutlinedTextField(value = name, onValueChange ={name = it},
                label = { Text("Domain") },
                modifier = Modifier.padding(8.dp))

            OutlinedTextField(value = rate, onValueChange ={rate = it},
                label = { Text("Rate") },
                modifier = Modifier.padding(8.dp))

            Button(onClick = {
                onClickSave()
            }) {
                Text(text = "Save")
            }
        }
    }
}
