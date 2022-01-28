package com.compose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.compose.R
import com.compose.ui.screens.friendList.ProfileScreen
import com.compose.ui.screens.movieList.MovieListScreen
import com.compose.ui.screens.movieList.MovieListViewModel
import com.compose.ui.theme.TopMoviesTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val movieListViewModel by inject<MovieListViewModel>()

    private val items = listOf(
        AppScreen.Currency,
        AppScreen.Profile
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityContent()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ActivityContent()
    }

    @Composable
    fun ActivityContent() {
        val navController = rememberNavController()

        TopMoviesTheme {
            Scaffold(
                bottomBar = { MyBottomBarNavigation(navController) }
            ) { innerPadding ->
                MyNavHost(navController, innerPadding)
            }
        }
    }

     @Composable
     private fun MyNavHost(navController: NavHostController, innerPadding: PaddingValues) {
        NavHost(
            navController = navController,
            startDestination = AppScreen.Currency.navRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.Currency.navRoute) {
                MovieListScreen(
                    context = baseContext,
                    viewModel = movieListViewModel
                )
            }
            composable(AppScreen.Profile.navRoute) {
                ProfileScreen(navController)
            }
        }
    }

    @Composable
    private  fun MyBottomBarNavigation(navController: NavHostController) {
        BottomNavigation {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            items.forEach { screen ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = screen.screenIcon,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(stringResource(screen.resourceId))
                    },
                    onClick = {
                        onNavigationItemClick(screen, navController)
                    },
                    selected = currentDestination?.hierarchy
                        ?.any { it.route == screen.navRoute  } == true,
                )
            }
        }
    }

    private fun onNavigationItemClick(screen: AppScreen, navController: NavHostController) {
        navController.navigate(screen.navRoute) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }


    sealed class AppScreen(
        val navRoute: String,
        @StringRes val resourceId: Int,
        val screenIcon: ImageVector
    ) {
        object Currency : AppScreen(NavRoute.Movies.route, R.string.top_movies, Icons.Filled.List)
        object Profile :AppScreen(NavRoute.Profile.route, R.string.profile, Icons.Filled.Person)
    }

    companion object {
        enum class NavRoute(val route: String) {
            Movies("Movies"),
            Profile("Profile")
        }
    }
}