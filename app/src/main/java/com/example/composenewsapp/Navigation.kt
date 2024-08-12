package com.example.composenewsapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.core.models.Article
import com.example.newdetails.ArticleDetailsRoute
import com.example.newslist.ui.NewsListRoute

@Composable
fun mainNavigation(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = BottomNavItems.News.route) {
        composable(BottomNavItems.News.route) {
            NewsListRoute {
                navController.currentBackStackEntry?.savedStateHandle?.set("article", it)
                navController.navigate("details")
            }
        }
        composable(BottomNavItems.Search.route) {

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