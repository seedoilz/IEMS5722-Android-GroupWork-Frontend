package com.example.iems5722.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.iems5722.R

data class ChatItem(
    val id: Int,
    val name: String,
    val lastMessage: String,
    val time: String,
    val profilePic: Int
)

// TODO the lastMessage
val sampleChats = listOf(
    ChatItem(1, "Alice", "Hey, how are you?", "22:30", com.example.iems5722.R.drawable.default_avatar),
    ChatItem(2, "Bob", "Let's meet tomorrow", "21:15", com.example.iems5722.R.drawable.default_avatar),
    ChatItem(3, "Charlie", "Check out this photo!", "20:45", com.example.iems5722.R.drawable.default_avatar),
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("chat/{chatName}") { backStackEntry ->
            val chatName = backStackEntry.arguments?.getString("chatName") ?: "Unknown"
            Log.d("ChatRoom", "Chat name: $chatName")
            ChatroomScreen(chatName, navController)
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = "Chats",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn {
            items(sampleChats) { chat ->
                ChatListItem(chat = chat, onClick = {
                    Log.d("Navigation", "Navigating to: chat/${chat.name}")
                    navController.navigate("chat/${chat.name}")
                })
                Divider(color = Color.Gray, thickness = 0.5.dp)
            }
        }
    }
}

@Composable
fun ChatListItem(chat: ChatItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = chat.profilePic),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .padding(end = 8.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = chat.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = chat.lastMessage,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }

        Text(
            text = chat.time,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}
