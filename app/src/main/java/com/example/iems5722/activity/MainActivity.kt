package com.example.iems5722.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.iems5722.ui.theme.IEMS5722AndroidGroupWorkFrontendTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.iems5722.component.AddFriendScreen
import com.example.iems5722.component.ContactsScreen
import com.example.iems5722.component.EditProfileScreen
import com.example.iems5722.component.HomeScreen
import com.example.iems5722.component.LoginScreen
import com.example.iems5722.component.ProfileScreen
import com.example.iems5722.component.RegisterScreen
import org.openapitools.client.apis.DefaultApi
import org.openapitools.client.infrastructure.ApiClient


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IEMS5722AndroidGroupWorkFrontendTheme {
                val navController = rememberNavController()
                val loggedIn = remember { mutableStateOf(false) }
                val apiService = ApiClient().createService(DefaultApi::class.java)

                Scaffold(
                    bottomBar = {
                        if (loggedIn.value) {
                            BottomNavigationBar(navController = navController)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable("login") { LoginScreen(navController,loggedIn, apiService) }
                        composable("register") { RegisterScreen(navController, apiService) }
                        composable("home") { HomeScreen() }
                        composable("contacts") { ContactsScreen(navController) }
                        composable("profile") { ProfileScreen(navController,loggedIn) }
                        composable("edit_profile") { EditProfileScreen(navController) }
                        composable("add_friend") { AddFriendScreen(navController) }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Chats") },
            selected = currentRoute == "home",
            onClick = {
                if (currentRoute != "home") {
                    navController.navigate("home") {
                        // 避免重复导航创建页面
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Contacts") },
            label = { Text("Contacts") },
            selected = currentRoute == "contacts",
            onClick = {
                if (currentRoute != "contacts") {
                    navController.navigate("contacts") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Menu, contentDescription = "Profile") },
            label = { Text("Settings") },
            selected = currentRoute == "profile",
            onClick = {
                if (currentRoute != "profile") {
                    navController.navigate("profile") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
    }
}

