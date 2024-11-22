package com.example.iems5722.component

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import okhttp3.Interceptor
import org.openapitools.client.apis.DefaultApi
import org.openapitools.client.infrastructure.ApiClient
import org.openapitools.client.models.User
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import org.openapitools.client.models.Result
import com.example.iems5722.R

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
            Toast.makeText(context, "用户ID缺失，请重新登录。", Toast.LENGTH_SHORT).show()
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
            return@LaunchedEffect
        }

        apiService.userDetailGet(userId).enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let {
                        val dataMap = response.body()?.data as Map<*, *>
                        println(it.toString())
                        userInfo.value = userInfo.value.copy(
                            name = dataMap["name"].toString(),
                            email = dataMap["email"].toString(),
                            avatarUrl = dataMap["avatarUrl"].toString()
                        )
                    } ?: run {
                        Toast.makeText(context, "获取用户详情失败。", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "错误: ${response.body()?.message ?: "未知错误"}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(context, "网络错误: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri.value = uri
        uri?.let {
            // 在这里实现将图片上传到服务器的逻辑
            Toast.makeText(context, "选择的头像: $uri", Toast.LENGTH_SHORT).show()
            // TODO: 实现图片上传API调用逻辑
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 200.dp)//头像位置
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = when {
                    imageUri.value != null -> rememberAsyncImagePainter(model = imageUri.value)
                    userInfo.value.avatarUrl?.isNotEmpty() == true && userInfo.value.avatarUrl != "null" -> rememberAsyncImagePainter(model = userInfo.value.avatarUrl)
                    else -> painterResource(id = R.drawable.default_avatar)
                },
                contentDescription = "用户头像",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable {
                        launcher.launch("image/*")
                    },
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userInfo.value.name ?: "未知用户",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = userInfo.value.email ?: "未提供电子邮件",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                navController.navigate("edit_profile")
                Toast.makeText(context, "编辑个人信息", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "编辑个人信息")
        }

        Button(
            onClick = {
                Toast.makeText(context, "修改密码被点击", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "修改密码")
        }

        Button(
            onClick = {
                clearUserId(context)
                navController.navigate("login") {
                    loggedIn.value = false
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text(text = "退出登录", color = MaterialTheme.colorScheme.onError)
        }
    }
}

fun getUserId(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    return sharedPreferences.getString("userId", null)
}

fun clearUserId(context: Context) {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        remove("userId")
        apply()
    }
}
