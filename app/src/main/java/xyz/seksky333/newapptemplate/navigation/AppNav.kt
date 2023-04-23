package xyz.seksky333.newapptemplate.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import xyz.seksky333.newapptemplate.ui.theme.primary
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import xyz.seksky333.newapptemplate.R
import xyz.seksky333.newapptemplate.main.MainViewModel

class AppNav {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { AppBottomNavigation(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AppNav(mainViewModel = mainViewModel, navController = navController)
        }
    }
}

@Composable
fun AppNav(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(Screen.MainScreen.route) {
//            SpendingScreen(
//                vm = mainViewModel.spendingViewModel
//            )
        }
        composable(Screen.DevScreen.route) {
//            DevScreen()
        }
    }
}

@Composable
fun AppBottomNavigation(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }

    // add a new screen here
    val navItems = listOf(Screen.MainScreen, Screen.DevScreen)
    val iconModifier = Modifier.size(24.dp)

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        navItems.forEachIndexed { index, screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            NavigationBarItem(
                icon = {
                    when (screen) {
                        Screen.MainScreen -> {
                            Icon(
                                modifier = iconModifier,
                                painter = painterResource(R.drawable.ic_launcher_background),
                                contentDescription = null
                            )
                        }

                        Screen.DevScreen -> {
                            Icon(
                                modifier = iconModifier,
                                painter = painterResource(R.drawable.ic_launcher_background),
                                contentDescription = null
                            )
                        }
                    }
                },
                label = { Text(screen.route) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = primary
                ),
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(screen.route) {
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
                })
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object MainScreen : Screen("main", R.string.main)
    object DevScreen : Screen("dev", R.string.dev)
}