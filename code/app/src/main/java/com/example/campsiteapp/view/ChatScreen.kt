/*
Display messages in a specific chat
Automatically opened when attempting to message another user
Outstanding issues:
- uses database time and not local time
- database access/send failures not handled
 */
package com.example.campsiteapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.campsiteapp.data.MessagesDatabase
import com.example.campsiteapp.data.ProfileDatabase
import com.example.campsiteapp.model.Chat
import com.example.campsiteapp.model.Message
import com.example.campsiteapp.model.User
import com.example.campsiteapp.view.components.ProfilePicture
import com.example.campsiteapp.view.components.ScreenScaffold
import kotlinx.coroutines.launch

/**
 * Displays messages belonging to particular chat, interface for message sending and receiving
 *
 * Gets or creates chat in database for [userDeviceId] and [recipientDeviceId]
 * Displays messages contained in chat, user can send new messages
 * Updated in real time
 *
 * @param onBack behavior upon pressing back button
 * @param userDeviceId the unique device id associated with the user
 * @param recipientDeviceId the unique device id associated with the user being messaged/ non-active user
 */
@Composable
fun ChatScreen(
    onBack: () -> Unit,
    userDeviceId: String,
    recipientDeviceId: String
){
    val messagesDatabase = MessagesDatabase()
    val profileDatabase = ProfileDatabase()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var userProfile: User? by remember { mutableStateOf(User()) }
    var recipientProfile: User? by remember { mutableStateOf(User()) }

    val context = LocalContext.current
    val initialChat by produceState(Chat(), context ){
        userProfile = profileDatabase.getProfile(userDeviceId)
        recipientProfile = profileDatabase.getProfile(recipientDeviceId)
        //need to get or create chat, reference id later
        value = messagesDatabase.openChat(
            listOf(userDeviceId, recipientDeviceId),
            listOf(userProfile?.profileImage ?: "", recipientProfile?.profileImage ?: ""),
            listOf(userProfile?.name ?: "", recipientProfile?.name ?: "")

        )
    }
    if (initialChat.chatId!="") {
        //only display composables after some info is retrieved
        val chatTitle = recipientProfile?.name ?: "User"

        //both chat and messages are updated in real time
        val messagesFlow = remember(initialChat.chatId) {
            messagesDatabase.messagesListener(initialChat.chatId)
        }
        val messages by messagesFlow.collectAsStateWithLifecycle(initialValue = emptyList())
        val chatUpdateFlow = remember(initialChat.chatId) {
            messagesDatabase.chatUpdateListener(initialChat.chatId)
        }
        //chat needs to be maintained so it can be updated more easily
        val chat by chatUpdateFlow.collectAsStateWithLifecycle(initialValue = initialChat)

        //scroll to newest message when a message is added
        LaunchedEffect(
            messages.size,
            //lastMessageId
        ) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(messages.lastIndex)
            }
        }

        var messageText by remember { mutableStateOf("") }
        ScreenScaffold(
            chatTitle,
            {
                onBack()
            },
            {},
            {
                Column(
                    modifier = Modifier
                        .padding(top = TopAppBarDefaults.TopAppBarExpandedHeight)
                ) {

                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
                        contentPadding = PaddingValues(
                            top = TopAppBarDefaults.TopAppBarExpandedHeight,
                            bottom = 8.dp
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(
                            items = messages,
                            key = { item -> item.messageId }
                        ) { message ->

                            //preloaded fields for message so database accesses aren't triggered on recomposition
                            var profileImage = ""
                            var name = ""
                            if ((message.senderId == userDeviceId)) {
                                if (userProfile != null) {
                                    profileImage = userProfile!!.profileImage
                                }
                                name = "You"
                            } else if ((message.senderId == recipientDeviceId) && (recipientProfile != null)) {
                                profileImage = recipientProfile!!.profileImage
                                name = recipientProfile!!.name
                            }
                            if (name == "") {
                                name = "User"
                            }

                            DisplayMessage(
                                profileImage,
                                message.messageText,
                                name,
                                message.timestamp?.toDate().toString()
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .imePadding()
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        OutlinedTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            placeholder = { Text("Message $chatTitle") },
                            modifier = Modifier
                                .padding(10.dp)
                                .weight(1f)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )
                        //Click to send the message
                        FilledIconButton(
                            onClick = {
                                if (messageText.isNotBlank()) {
                                    val message = Message()
                                    message.messageText = messageText
                                    message.senderId = userDeviceId
                                    message.senderName = userProfile?.name ?: "User"
                                    scope.launch {
                                        messagesDatabase.addMessageToChat(chat, message)
                                    }
                                    messageText = ""
                                }
                            },
                            modifier = Modifier
                                //.weight(1f)
                                .size(36.dp)
                                .align(Alignment.CenterVertically),
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = Color.Green,
                                contentColor = Color.White,
                            ),

                            ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                "Send button",
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(6.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}

/**
 * Displays message
 *
 * Formats relevant profile info of sender and message contents and timestamp into a readable list entry
 *
 * @param profileImage profile image of sender
 * @param messageText the text contained in the message
 * @param username name of sender
 * @param timestamp the time the message was added to the database
 */
@Composable
fun DisplayMessage(
    profileImage: String,
    messageText: String,
    username: String,
    timestamp: String
){
        Row(
            modifier = Modifier
               .padding(15.dp)
        ) {
            ProfilePicture(
                modifier = Modifier
                    .align(Alignment.Top)
                    .offset(y = 2.dp)
                ,
                profileImage,
                36.dp,
                "${username}'s Profile image"
            ) {
            }
            Spacer(
                modifier = Modifier
                    .width(10.dp)
            )

            Column(
                modifier = Modifier
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        username,
                        modifier = Modifier
                            .padding(horizontal = 5.dp, vertical = 0.dp)
                            .weight(1f)
                        ,
                        textAlign = TextAlign.Left,
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    Text(
                        timestamp,
                        modifier = Modifier
                            .padding(horizontal = 5.dp, vertical = 0.dp)
                            .align(Alignment.Top)
                        ,
                        textAlign = TextAlign.Right,
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        maxLines = 1
                    )
                }
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.Top
                ) {
                   Text(
                        messageText,
                        modifier = Modifier
                            .padding(horizontal = 5.dp, vertical = 4.dp),
                        textAlign = TextAlign.Left,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }
            }
        }
}