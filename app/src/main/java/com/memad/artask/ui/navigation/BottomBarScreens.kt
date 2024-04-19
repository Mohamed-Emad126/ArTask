package com.memad.artask.ui.navigation

import com.memad.artask.R

sealed class BottomBarScreens(
    val route: String,
    val title: String,
    val icon : Int,
) {
    object Home : BottomBarScreens(
        route = "articles_list",
        title = "Articles",
        icon = R.drawable.feed
    )

    object Favorites : BottomBarScreens(
        route = "favorites",
        title = "favorites",
        icon = R.drawable.favorite
    )
}