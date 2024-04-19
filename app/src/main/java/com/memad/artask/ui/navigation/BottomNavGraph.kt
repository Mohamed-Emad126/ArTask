package com.memad.artask.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.memad.artask.ui.screens.favorites_list.FavoritesScreen
import com.memad.artask.ui.screens.articles_list.ArticlesListScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreens.Home.route
    ) {
        composable(route = BottomBarScreens.Home.route) {
            ArticlesListScreen()
        }
        composable(route = BottomBarScreens.Favorites.route) {
            FavoritesScreen()
        }
    }
}