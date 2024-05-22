package com.example.finflow.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.finflow.MainViewModel
import com.example.finflow.presentation.signInToDashboard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel){
    val context = LocalContext.current
//    signInToDashboard(context)
    val title by remember {
        mutableStateOf("CureIt")
    }
    var isFabVisible by remember {
        mutableStateOf(false)
    }

    val items = listOf(
        BottomNavigationItem(
            title = "Dashboard",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),

        BottomNavigationItem(
            title = "Earn",
            selectedIcon = Icons.Filled.CreditCard,
            unselectedIcon = Icons.Outlined.CreditCard
        ),

        BottomNavigationItem(
            title = "Debit",
            selectedIcon = Icons.Filled.Apps,
            unselectedIcon = Icons.Outlined.Apps
        ),

        BottomNavigationItem(
            title = "Stats",
            selectedIcon = Icons.Filled.AutoGraph,
            unselectedIcon = Icons.Outlined.AutoGraph
        )
    )
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)

    }

    val scrollBehavior =TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            LargeTopAppBar(title = {
                Text(text = title)
            },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                    }
                })
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = (item == items[selectedItemIndex]),
                            onClick = { selectedItemIndex = index },
                            label = {
                                Text(text = item.title)
                            },
                            icon = {
                                Icon(
                                if (item == items[selectedItemIndex])
                                    item.selectedIcon
                                else
                                    item.unselectedIcon,
                                contentDescription = null)
                            }
                        )
                    }
                }
        },
        floatingActionButton = {
            if (isFabVisible) {
                FloatingActionButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.AutoStories, contentDescription = null)
                }
            }
        },
    ) {paddingValues ->
        AnimatedContent(targetState = selectedItemIndex) {
            when(it){
                0 -> DashboardScreen(viewModel = viewModel, paddingValues = paddingValues)
                1 -> GainScreen()
//                2 -> DebitScreen()
//                3 -> StatsScreen()
            }

        }
    }
}