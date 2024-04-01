package com.example.androidcoolboxryhma2

import androidx.activity.compose.setContent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.FragmentManager



class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val navController = rememberNavController()


            NavHost(navController = navController, startDestination = "graphTest") {

                composable("loginScreen") {
                    LoginScreen {

                    }
                }
                composable("graphTest"){
                    GraphTest {

                    }
                }
            }
        }
    }

}


    /******************
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    setContentView(R.layout.activity_main)

// Setup Navigation Drawer
drawerLayout = findViewById(R.id.drawer_layout)
val navHostFragment =
    supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
navController = navHostFragment.navController

val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)

// Setup ActionBarDrawerToggle
val toggle = ActionBarDrawerToggle(
    this, drawerLayout, findViewById(R.id.toolbar),
    R.string.navigation_drawer_open, R.string.navigation_drawer_close
)
drawerLayout.addDrawerListener(toggle)
toggle.syncState()
}

override fun onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
        drawerLayout.closeDrawer(GravityCompat.START)
    } else {
        super.onBackPressed()
    }
}
     ******************************************/