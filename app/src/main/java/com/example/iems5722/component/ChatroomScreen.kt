package com.example.iems5722.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.iems5722.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

// ChatRoomScreen 显示聊天界面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatroomScreen(chatName: String, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Chat with $chatName", fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp)
            ) {
                // 模拟聊天内容
                MessageItem(
                    message = "哈哈哈哈哈哈哈哈哈",
                    isSentByCurrentUser = true,
                    avatar = R.drawable.default_avatar
                )

                MessageItem(
                    message = "哈哈哈哈哈哈哈哈",
                    isSentByCurrentUser = false,
                    avatar = R.drawable.default_avatar
                )

                Spacer(modifier = Modifier.weight(1f))

                // 底部输入框
                ChatInputField()
            }
        }
    )
}

@Composable
fun MessageItem(message: String, isSentByCurrentUser: Boolean, avatar: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isSentByCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isSentByCurrentUser) {
            Image(
                painter = painterResource(id = avatar),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .padding(4.dp)
            )
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isSentByCurrentUser) Color(0xFFDCF8C6) else Color(0xFFFFFFFF)
                )
                .padding(12.dp)
        ) {
            Text(
                text = message,
                fontSize = 16.sp,
                color = Color.Black
            )
        }

        if (isSentByCurrentUser) {
            Image(
                painter = painterResource(id = avatar),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun ChatInputField() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            placeholder = { Text(text = "Type a message...") }
        )

        IconButton(
            onClick = { /* TODO: 添加发送消息逻辑 */ },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Send",
                tint = Color(0xFF128C7E)
            )
        }
    }
}
