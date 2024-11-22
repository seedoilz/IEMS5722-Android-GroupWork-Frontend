package com.example.iems5722.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.openapitools.client.apis.DefaultApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.openapitools.client.models.Result

@Composable
fun ForgotPasswordScreen(navController: NavController, apiService: DefaultApi) {
    val email = remember { mutableStateOf("") }
    val message = remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Forgot Password")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        message.value?.let {
            val messageColor = if (it.startsWith("Error")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            Text(
                text = it,
                color = messageColor,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                if (email.value.isBlank()) {
                    message.value = "Please enter your email address."
                    return@Button
                }

//                // 调用忘记密码API逻辑
//                apiService.userForgotPasswordPost(email.value).enqueue(object : Callback<Result> {
//                    override fun onResponse(call: Call<Result>, response: Response<Result>) {
//                        if (response.isSuccessful && response.body()?.code == 200) {
//                            message.value = "Password reset link has been sent to your email."
//                        } else {
//                            message.value = "Error: ${response.body()?.message ?: "Unknown error"}"
//                        }
//                    }
//
//                    override fun onFailure(call: Call<Result>, t: Throwable) {
//                        message.value = "Network request failed: ${t.message}"
//                    }
//                })

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send Reset Link")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                // 返回登录页面
                navController.popBackStack()
            }
        ) {
            Text("Back to Login")
        }
    }
}
