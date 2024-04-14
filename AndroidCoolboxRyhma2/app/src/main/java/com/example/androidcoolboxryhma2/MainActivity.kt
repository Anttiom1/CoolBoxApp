package com.example.androidcoolboxryhma2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    gesturesEnabled = true,
                    drawerState = drawerState,
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    drawerContent = {
                        ModalDrawerSheet {
                            Spacer(modifier = Modifier.height(16.dp))
                            NavigationDrawerItem(
                                label = { Text(text = "Home") },
                                selected = false,
                                onClick = {
                                    navController.navigate("homeScreen")
                                    scope.launch { drawerState.close() }
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Home,
                                        contentDescription = "Home"
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            NavigationDrawerItem(
                                label = { Text(text = "Graphs") },
                                selected = false,
                                onClick = {
                                    navController.navigate("graphScreen")
                                    scope.launch { drawerState.close() }
                                },
                                icon = {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.baseline_show_chart_24),
                                        contentDescription = "Graph"
                                    )
                                }
                            )
                        }
                    }
                ) {
                    NavHost(navController = navController, startDestination = "loginScreen") {
                        composable("loginScreen") {
                            LoginScreen(
                                onLoginClick = {
                                    navController.navigate("homeScreen")
                                })
                        }
                        composable("homeScreen") {
                            HomeScreen(
                                goToGraph = {
                                    navController.navigate("graphScreen")
                                }, logOut = {
                                    navController.navigate("loginScreen")
                                })
                        }
                        composable("graphScreen") {
                            GraphScreen(
                                onMenuClick = {scope.launch { drawerState.open() }},
                                goToHome = {
                                    navController.navigate("homeScreen")
                                })
                        }
                    }
                }
            }
        }
    }
}
