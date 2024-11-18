package com.example.iems5722.component

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
fun AddFriendScreen(navController: NavController) {
    val context = LocalContext.current
    val userId = getUserId(context)?.toIntOrNull()
    var friendName by remember { mutableStateOf("") }
    var friendId by remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }
    val token = context.getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("token", null)

    val authorizationInterceptor = Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        chain.proceed(request)

    }

    val apiService = ApiClient().addAuthorization("BearerToken", authorizationInterceptor).createService(DefaultApi::class.java)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Friend") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 输入好友 ID
                OutlinedTextField(
                    value = friendId,
                    onValueChange = { friendId = it },
                    label = { Text("Friend ID") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 输入好友名称
                OutlinedTextField(
                    value = friendName,
                    onValueChange = { friendName = it },
                    label = { Text("Friend Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (friendId.isBlank() || friendName.isBlank()) {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (userId != null) {
                            addFriend(apiService, userId, friendId.toIntOrNull(), friendName, context, navController, isLoading)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading.value // Disable when loading
                ) {
                    if (isLoading.value) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Add Friend")
                    }
                }
            }
        }
    )
}

private fun addFriend(
    apiService: DefaultApi,
    userId: Int,
    friendId: Int?,
    friendName: String,
    context: Context,
    navController: NavController,
    isLoading: MutableState<Boolean>
) {
    if (friendId == null) {
        Toast.makeText(context, "Invalid Friend ID", Toast.LENGTH_SHORT).show()
        return
    }

    isLoading.value = true // Set loading state to true
    apiService.friendAddPost(userId = userId, friendId = friendId, friendName = friendName).enqueue(object : Callback<Result> {
        override fun onResponse(call: Call<Result>, response: Response<Result>) {
            isLoading.value = false // Reset loading state
            if (response.isSuccessful && response.body()?.code == 200) {
                Toast.makeText(context, "Friend added successfully", Toast.LENGTH_SHORT).show()
                navController.popBackStack() // Navigate back
            } else {
                Toast.makeText(context, "Failed to add friend: ${response.body()?.message ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<Result>, t: Throwable) {
            isLoading.value = false // Reset loading state
            Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}
