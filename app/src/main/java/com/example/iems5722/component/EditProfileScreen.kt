package com.example.iems5722.component

import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import androidx.compose.foundation.layout.*
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
import org.openapitools.client.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun EditProfileScreen(navController: NavController) {
    val context = LocalContext.current

    // 定义用户信息状态
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val avatarUrl = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val userId = getUserId(context)?.toIntOrNull()
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
    // 加载用户详情
    LaunchedEffect(userId) {
        if (userId != null) {
            apiService.userDetailGet(userId).enqueue(object : Callback<org.openapitools.client.models.Result> {
                override fun onResponse(call: Call<org.openapitools.client.models.Result>, response: Response<org.openapitools.client.models.Result>) {
                    if (response.isSuccessful && response.body()?.code == 200) {
                        response.body()?.data?.let { data ->
                            val dataMap = data as? Map<String, Any>
                            name.value = dataMap?.get("name").toString()
                            email.value = dataMap?.get("email").toString()
                        }
                    } else {
                        errorMessage.value = "Failed to load user details."
                    }
                }

                override fun onFailure(call: Call<org.openapitools.client.models.Result>, t: Throwable) {
                    errorMessage.value = "Network error: ${t.message}"
                }
            })
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Edit Profile", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // 用户名输入框
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 邮箱输入框
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 错误信息提示
        errorMessage.value?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // 保存按钮
        Button(
            onClick = {
                val user = User(
                    id = userId,
                    name = name.value,
                    email = email.value,
                    avatarUrl = avatarUrl.value
                )
                apiService.userUpdatePost(user).enqueue(object : Callback<org.openapitools.client.models.Result> {
                    override fun onResponse(call: Call<org.openapitools.client.models.Result>, response: Response<org.openapitools.client.models.Result>) {
                        if (response.isSuccessful && response.body()?.code == 200) {
                            Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                            navController.navigate("profile") // 返回 Profile 页面
                        } else {
                            errorMessage.value = "Update failed: ${response.body()?.message ?: "Unknown error"}"
                        }
                    }

                    override fun onFailure(call: Call<org.openapitools.client.models.Result>, t: Throwable) {
                        errorMessage.value = "Network error: ${t.message}"
                    }
                })
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save Changes")
        }
    }
}
