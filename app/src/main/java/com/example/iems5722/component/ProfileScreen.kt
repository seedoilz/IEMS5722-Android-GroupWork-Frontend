package com.example.iems5722.component

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import okhttp3.Interceptor
import org.openapitools.client.apis.DefaultApi
import org.openapitools.client.infrastructure.ApiClient
import org.openapitools.client.models.User
import retrofit2.Callback

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import retrofit2.Call

import retrofit2.Response
import org.openapitools.client.models.Result


@Composable
fun ProfileScreen(navController: NavController, loggedIn: MutableState<Boolean>) {
    val context = LocalContext.current
    val userInfo = remember { mutableStateOf(User(name = "Unknown User", avatarUrl = "", email = "Unknown Email")) }
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
    // 拉取用户信息
    LaunchedEffect(userId) {
        if (userId == null) {
            Toast.makeText(context, "User ID is missing. Please log in again.", Toast.LENGTH_SHORT).show()
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
            return@LaunchedEffect
        }

        apiService.userDetailGet(userId).enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    // 成功获取用户详情
                    response.body()?.data?.let {

                        println(it.toString())
                        userInfo.value.name = response.body()?.data?.get("name").toString()
                        userInfo.value.email = response.body()?.data?.get("email").toString()

                    } ?: run {
                        Toast.makeText(context, "Failed to retrieve user details.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 获取用户详情失败
                    Toast.makeText(context, "Error: ${response.body()?.message ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                // 网络请求失败
                Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 显示用户头像
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(userInfo.value.avatarUrl),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable {
                        // 上传头像逻辑
                        Toast.makeText(context, "Upload avatar clicked", Toast.LENGTH_SHORT).show()
                    },
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 显示用户名
        Text(
            text = userInfo.value.name ?: "Unknown User",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 显示用户邮箱
        Text(
            text = userInfo.value.email ?: "No Email Provided",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.weight(1f))
        // 改profile按钮
        Button(
            onClick = {
                // 跳转到修改profile界面逻辑
                navController.navigate("edit_profile")
                Toast.makeText(context, "Edit Profile", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Edit Profile")
        }
        // 改密码按钮
        Button(
            onClick = {
                // 跳转到修改密码界面逻辑
                Toast.makeText(context, "Change password clicked", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Change Password")
        }

        // 退出登录按钮
        Button(
            onClick = {
                clearUserId(context)
                navController.navigate("login") {
                    loggedIn.value = false
                    popUpTo("home") { inclusive = true } // 清除返回栈
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text(text = "Log Out", color = MaterialTheme.colorScheme.onError)
        }
    }
}

// 获取用户 ID
fun getUserId(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    return sharedPreferences.getString("userId", null)
}

// 清除用户 ID
fun clearUserId(context: Context) {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        remove("userId")
        apply()
    }
}
