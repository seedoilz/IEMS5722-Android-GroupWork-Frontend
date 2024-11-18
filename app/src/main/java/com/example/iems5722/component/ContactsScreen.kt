package com.example.iems5722.component

import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import okhttp3.Interceptor
import org.openapitools.client.apis.DefaultApi
import org.openapitools.client.infrastructure.ApiClient
import org.openapitools.client.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(navController: NavController) {
    val context = LocalContext.current
    val userId = getUserId(context)?.toIntOrNull()
    val contacts = remember { mutableStateListOf<Map<String, Any>>() }
    val predefinedContacts = listOf(
        mapOf("id" to 1, "friendName" to "Alice"),
        mapOf("id" to 1, "friendName" to "Andy"),
        mapOf("id" to 2, "friendName" to "Bob"),
        mapOf("id" to 3, "friendName" to "Charlie"),
        mapOf("id" to 1, "friendName" to "Zoe"),
        mapOf("id" to 5, "friendName" to "Eve")
    )
    var isLoading by remember { mutableStateOf(false) }
    val token = context.getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", null)

    val authorizationInterceptor = Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        chain.proceed(request)
    }

    val apiService = ApiClient().addAuthorization("BearerToken", authorizationInterceptor).createService(DefaultApi::class.java)

    // Load contacts
    LaunchedEffect(userId) {
        if (userId != -1) {
            isLoading = true
            contacts.addAll(predefinedContacts)
            apiService.friendAllPost(userId).enqueue(object : Callback<Result> {
                override fun onResponse(call: Call<Result>, response: Response<Result>) {
                    isLoading = false
                    if (response.isSuccessful && response.body()?.code == 200) {
                        val data = response.body()?.data as? List<Map<String, Any>>
                        data?.let { contacts.addAll(it) }
                    } else {
                        Toast.makeText(context, "Failed to load contacts", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Result>, t: Throwable) {
                    isLoading = false
                    Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(context, "User ID is missing. Please log in again.", Toast.LENGTH_SHORT).show()
            navController.navigate("login")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacts") },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate("add_friend")
                            Toast.makeText(context, "Add Friend clicked", Toast.LENGTH_SHORT).show()
                        }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Friend")
                    }
                }
            )
        },
        content = { padding ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (contacts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No contacts found", fontSize = 16.sp)
                }
            } else {
                val groupedContacts = contacts.groupBy {
                    (it["friendName"] as? String)?.firstOrNull()?.uppercase() ?: "#"
                }.toSortedMap()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    groupedContacts.forEach { (initial, group) ->
                        item {
                            Text(
                                text = initial,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                fontSize = 18.sp
                            )
                        }
                        items(group) { contact ->
                            ContactItem(contact, navController)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ContactItem(contact: Map<String, Any>, navController: NavController) {
    val name = contact["friendName"] as? String ?: "Unknown"
    val id = (contact["id"] as? Int) ?: -1

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { navController.navigate("friend_detail/$id") }, // Navigate when the row is clicked
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar placeholder
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(MaterialTheme.shapes.medium) // Rounded corners
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.firstOrNull()?.toString() ?: "?", // Show first letter as placeholder
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Name
        Text(
            text = name,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
    }
}
