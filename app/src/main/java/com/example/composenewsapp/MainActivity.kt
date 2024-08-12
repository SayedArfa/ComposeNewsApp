package com.example.composenewsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composenewsapp.ui.theme.ComposeNewsAppTheme
import com.example.newslist.ui.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeNewsAppTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    BottomNavigation {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        bottomNavItems.forEach { screen ->
                            BottomNavigationItem(
                                icon = {
                                    Icon(
                                        imageVector = screen.imageVector,
                                        contentDescription = null
                                    )
                                },
                                label = { Text(text = stringResource(id = screen.stringResourceId)) },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(route = screen.route) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    }
                }) { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        mainNavigation(navController)
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
    data object News : BottomNavItems("news", R.string.news, Icons.Default.Home)
    data object Search : BottomNavItems("search", R.string.search, Icons.Default.Search)
    data object Favorite : BottomNavItems("fav", R.string.favorites, Icons.Default.Favorite)
}