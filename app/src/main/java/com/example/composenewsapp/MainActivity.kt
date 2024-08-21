package com.example.composenewsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composenewsapp.ui.theme.ComposeNewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeNewsAppTheme {
                val navController = rememberNavController()
                val snackBarHostState = remember { SnackbarHostState() }
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination


                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {

                    val hideBottomBar = remember {
                        mutableStateOf(false)
                    }
                    when (currentDestination?.route) {
                        "details" -> {
                            hideBottomBar.value = false
                        }

                        else -> {
                            hideBottomBar.value = true
                        }
                    }
                    if (hideBottomBar.value) {
                        BottomNavigation(
                            elevation = 5.dp
                        ) {
                            bottomNavItems.forEach { screen ->
                                val selected = currentDestination?.route == screen.route
                                BottomNavigationItem(
                                    icon = {
                                        Icon(
                                            imageVector = screen.imageVector,
                                            contentDescription = null,
                                            tint = if (selected) Color.Green else Color.Black
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = stringResource(id = screen.stringResourceId),
                                            color = if (selected) Color.Green else Color.Black
                                        )
                                    },
                                    selected = selected,
                                    onClick = {
                                        navController.navigate(route = screen.route) {
                                            popUpTo(navController.graph.startDestinationId)
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                        }

                    }
                }, snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        MainNavigation(navController, snackBarHostState)
                    }
                }
            }
        }
    }
}


val bottomNavItems =
    listOf(BottomNavItems.News, BottomNavItems.Search, BottomNavItems.Favorite)

sealed class BottomNavItems(
    val route: String,
    @StringRes val stringResourceId: Int,
    val imageVector: ImageVector
) {
    data object News : BottomNavItems("news", R.string.news, Icons.Filled.Home)
    data object Search : BottomNavItems("search", R.string.search, Icons.Filled.Search)
    data object Favorite : BottomNavItems("fav", R.string.favorites, Icons.Filled.Favorite)
}