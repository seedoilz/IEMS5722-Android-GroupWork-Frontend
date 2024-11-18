package com.example.iems5722.component

import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun FriendDetailScreen(friendId: Int, navController: NavController) {
    val context = LocalContext.current
    var friendName by remember { mutableStateOf("Unknown") }
    var friendEmail by remember { mutableStateOf("Unknown Email") } // Added for email
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
    // Fetch friend details
    LaunchedEffect(friendId) {
        isLoading = true
        apiService.friendDetailPost(friendId).enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                isLoading = false
                if (response.isSuccessful && response.body()?.code == 200) {
                    val data = response.body()?.data as? Map<String, Any>
                    friendName = data?.get("friendName") as? String ?: "Unknown"
                    friendEmail = data?.get("friendEmail") as? String ?: "Unknown Email" // Extract email
                } else {
                    Toast.makeText(context, "Failed to load friend details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                isLoading = false
                Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Friend Details") }
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
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Friend Name: $friendName", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Friend Email: $friendEmail", fontSize = 16.sp) // Display email
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { /* Start Private Chat */ }) {
                        Text("Start Chat")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
//                        deleteFriend(apiService, friendId, mutableListOf(), context)
                        navController.popBackStack()
                    }) {
                        Text("Delete Friend")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        navController.navigate("contacts") {
                            popUpTo("contacts") { inclusive = true }
                        }
                    }) {
                        Text("Back to Contacts")
                    }

                }
            }
        }
    )
}
