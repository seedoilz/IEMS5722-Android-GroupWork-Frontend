package com.example.iems5722.component

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.openapitools.client.models.UserVO
import org.openapitools.client.apis.DefaultApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.openapitools.client.models.Result


@Composable
fun LoginScreen(
    navController: NavController,
    loggedIn: MutableState<Boolean>,
    apiService: DefaultApi
) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val loginError = remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current // 获取当前 Context

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Sign in")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("User Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 显示错误信息
        loginError.value?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                if (username.value.isBlank() || password.value.isBlank()) {
                    loginError.value = "Please input your user name and password"
                    return@Button
                }
                // 调用登录API逻辑
                val userVO = UserVO(name = username.value, password = password.value)
                apiService.userSignInPost(userVO).enqueue(object : Callback<Result> {
                    override fun onResponse(call: Call<Result>, response: Response<Result>) {
                        if (response.isSuccessful && response.body()?.code == 200) {
                            val dataMap = response.body()?.data as Map<String, Any>
                            // 获取返回的 token
                            val token = dataMap.get("token")
                            val userId = dataMap.get("id")
                            // 保存 token 到 SharedPreferences
                            token?.let {
                                saveTokenToPreferences(context, it.toString(), userId.toString())
                                loggedIn.value = true
                                navController.navigate("home")
                            } ?: run {
                                loginError.value = "Failed to retrieve token."
                            }
                        } else {
                            // 登录失败
                            loginError.value = "Login failed: ${response.body()?.message ?: "Unknown error"}"
                        }
                    }

                    override fun onFailure(call: Call<Result>, t: Throwable) {
                        // 网络请求失败
                        loginError.value = "Network request failed: ${t.message}"
                    }
                })
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign in")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = {
                    // 点击注册按钮，跳转到注册界面
                    navController.navigate("register")
                }
            ) {
                Text("Register here")
            }

            TextButton(
                onClick = {
                    // 点击忘记密码按钮，跳转到忘记密码界面
                    navController.navigate("forgot_password")
                }
            ) {
                Text("Forgot Password?")
            }
        }
    }
}

fun saveTokenToPreferences(context: Context, token: String, userId: String) {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("token", token)
    editor.putString("userId", userId)
    println(userId)
    editor.apply() // 异步提交
}

