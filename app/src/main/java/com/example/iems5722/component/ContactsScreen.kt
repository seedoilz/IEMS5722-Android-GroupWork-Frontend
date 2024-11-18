package com.example.iems5722.component

import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var isLoading by remember { mutableStateOf(false) }
    val token = context.getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", null)
    println("Retrieved token: $token")

    val authorizationInterceptor = Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        chain.proceed(request)

    }

    val apiService = ApiClient().addAuthorization("BearerToken", authorizationInterceptor).createService(DefaultApi::class.java)
    // 加载联系人
    LaunchedEffect(userId) {
        if (userId != -1) {
            isLoading = true
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(contacts) { contact ->
                        ContactItem(contact, apiService, contacts)
                    }
                }
            }
        }
    )
}

@Composable
fun ContactItem(contact: Map<String, Any>, apiService: DefaultApi, contacts: MutableList<Map<String, Any>>) {
    val context = LocalContext.current
    val name = contact["friendName"] as? String ?: "Unknown"
    val id = (contact["id"] as? Double)?.toInt() // 修复类型转换错误

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name, modifier = Modifier.weight(1f), fontSize = 16.sp)
        Button(onClick = {
            id?.let { deleteFriend(apiService, it, contacts, context) }
        }) {
            Text("Delete")
        }
    }
}

private fun deleteFriend(apiService: DefaultApi, friendId: Int, contacts: MutableList<Map<String, Any>>, context: android.content.Context) {
    apiService.friendDeletePost(friendId).enqueue(object : Callback<Result> {
        override fun onResponse(call: Call<Result>, response: Response<Result>) {
            if (response.isSuccessful && response.body()?.code == 200) {
                contacts.removeAll { it["id"] == friendId }
                Toast.makeText(context, "Friend deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to delete friend", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<Result>, t: Throwable) {
            Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}
