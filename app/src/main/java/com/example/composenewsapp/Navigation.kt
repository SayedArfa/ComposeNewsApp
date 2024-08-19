package com.example.composenewsapp


import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.core.models.Article
import com.example.newdetails.ArticleDetailsRoute
import com.example.newslist.ui.NewsListRoute
import com.example.newslist.ui.SearchNewsRoute

@Composable
fun MainNavigation(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    suspend fun onShowSnackBar(message: String, action: String?, onActionPerformed: () -> Unit) {
        val result = snackbarHostState.showSnackbar(
            message,
            actionLabel = action,
            duration = SnackbarDuration.Indefinite
        )
        when (result) {
            SnackbarResult.Dismissed -> {
                Log.d("snackBar", "Dismissed")
            }

            SnackbarResult.ActionPerformed -> {
                Log.d("snackBar", "Performed")
                onActionPerformed()
            }
        }
    }
    NavHost(navController = navController, startDestination = BottomNavItems.News.route) {
        composable(BottomNavItems.News.route) {
            NewsListRoute(onItemClick = {
                navController.currentBackStackEntry?.savedStateHandle?.set("article", it)
                navController.navigate("details")
            }, onShowSnackBar = ::onShowSnackBar)
        }
        composable(BottomNavItems.Search.route) {
            SearchNewsRoute(onItemClick = {
                navController.currentBackStackEntry?.savedStateHandle?.set("article", it)
                navController.navigate("details")
            }, onShowSnackBar = ::onShowSnackBar)
        }
        composable(BottomNavItems.Favorite.route) {

        }
        composable("details") {
            val article =
                navController.previousBackStackEntry?.savedStateHandle?.get<Article>("article")
            ArticleDetailsRoute(article = article) {
                navController.popBackStack()
            }
        }
    }


}
