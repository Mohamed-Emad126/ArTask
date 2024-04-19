package com.memad.artask.ui.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Straight
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.items.dropletbutton.DropletButton
import com.memad.artask.ui.navigation.BottomBarScreens
import com.memad.artask.ui.navigation.BottomNavGraph
import com.memad.artask.ui.theme.Purple41

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { DropletButtonNavBar(navController = navController) },
        contentColor = Color.Transparent,
        content = { paddingValues ->
            val heightToDecrease = 35.dp //Custom shape's notch height to decrease from bottom padding
            Box(Modifier.fillMaxSize().padding(
                PaddingValues(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding() - heightToDecrease)
            )) {
                Box(Modifier.fillMaxSize().padding()) {
                    BottomNavGraph(navController = navController)
                }
            }
        }
    )
}


@Composable
fun DropletButtonNavBar(navController: NavController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val screens = listOf(
        BottomBarScreens.Home,
        BottomBarScreens.Favorites
    )
    AnimatedNavigationBar(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .height(80.dp),
        selectedIndex = selectedItem,
        ballColor = MaterialTheme.colorScheme.primary,
        barColor = Purple41,
        cornerRadius = shapeCornerRadius(25.dp),
        ballAnimation = Straight(tween(500, easing = FastOutSlowInEasing)),
        indentAnimation = Height(
            indentWidth = 56.dp,
            indentHeight = 15.dp,
            animationSpec = tween(
                1000,
                easing = { OvershootInterpolator().getInterpolation(it) })
        )
    ) {
        screens.forEachIndexed { index, it ->
            DropletButton(
                modifier = Modifier.fillMaxSize(),
                isSelected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(it.route){
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                          },
                icon = it.icon,
                dropletColor = MaterialTheme.colorScheme.primary,
                animationSpec = tween(durationMillis = 500, easing = LinearEasing),
                iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}