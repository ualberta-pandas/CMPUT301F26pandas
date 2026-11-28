/*
Display chats for a specific user
Outstanding issues:
- uses database time and not local time
- database access/send failures not handled
- profile details not updated (only updated upon opening chat)
 */
package com.example.campsiteapp.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.campsiteapp.data.ProfileDatabase
import com.example.campsiteapp.model.User
import com.example.campsiteapp.view.components.ProfilePicture
import com.example.campsiteapp.view.components.ScreenScaffold
import com.example.campsiteapp.data.MessagesDatabase
import com.example.campsiteapp.model.Chat
import kotlinx.coroutines.launch

/**
 * Displays chats belonging to particular user, interface for opening chats
 *
 * Fetches user's chats
 * Displays previews of user's chats
 * Clicking preview opens chat
 *
 * @param onBack behavior upon pressing back button
 * @param userDeviceId the unique device id associated with the user
 */
@Composable
fun MessagesScreen(
    onBack: () -> Unit,
    userDeviceId: String
){
    var currentChatToDisplay:Chat? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    val messagesDatabase = MessagesDatabase()
    val listState = rememberLazyListState()

    //chats are updated in real time
    val chatsFlow = remember(userDeviceId) {
        messagesDatabase.chatsListener(userDeviceId)
    }
    val chats by chatsFlow.collectAsStateWithLifecycle(initialValue = emptyList())

    LaunchedEffect(
        chats.size,
        //lastMessageId
    ) {
        if (chats.isNotEmpty()) {
            //listState.animateScrollToItem(0)
        }
    }


    if (currentChatToDisplay == null) {
        ScreenScaffold(
            "Messages",
            {
                onBack()
            },
            {},
            {
                Column(
                    modifier = Modifier
                        .padding(top = TopAppBarDefaults.TopAppBarExpandedHeight)
                ) {

                    if(chats.isNotEmpty()) {
                        LazyColumn(
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                            contentPadding = PaddingValues(
                                top = TopAppBarDefaults.TopAppBarExpandedHeight,
                                bottom = 8.dp
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            items(
                                items = chats,
                                key = { item -> item.chatId }
                            ) { chat ->
                                val otherUserIndex =
                                    chat.userIds.indexOf(chat.getOtherUserId(userDeviceId))
                                val otherUserName = if (chat.userNames[otherUserIndex]!=""){
                                    chat.userNames[otherUserIndex]} else {
                                        "User"
                                }
                                val lastMessageSenderName = if (chat.lastMessageSenderId != "") {
                                    if (chat.lastMessageSenderId == userDeviceId) {
                                        "You"
                                    } else {
                                        val sender = chat.userNames[chat.userIds.indexOf(chat.lastMessageSenderId)]
                                        if (sender!="") {
                                            chat.userNames[chat.userIds.indexOf(chat.lastMessageSenderId)]
                                        } else {
                                            "User"
                                        }
                                    }
                                } else {
                                    "User"
                                }
                                DisplayChatPreview(
                                    chat.userProfileImages[otherUserIndex],
                                    otherUserName,
                                    lastMessageSenderName,
                                    chat.lastMessageText,
                                    chat.lastMessageTimestamp?.toDate().toString(),
                                    onClick = {
                                        currentChatToDisplay = chat
                                    }
                                )
                            }
                        }
                    } else {
                        Text(
                            "You have no messages."
                        )
                    }
                }
            }
        )
    }else{
        currentChatToDisplay?.let { ChatScreen({ currentChatToDisplay = null }, userDeviceId, it.getOtherUserId(userDeviceId)) }

    }
}

/**
 * Displays chat preview
 *
 * Formats relevant info of conversation, message contents and timestamp into a readable list entry
 *
 * @param profileImage profile image of other user
 * @param username name of other user
 * @param messageSenderName name of user who sent the last message
 * @param messageText the text contained in the message
 * @param timestamp the time the message was added to the database
 * @param onClick action to perform when preview is clicked
 */
@Composable
fun DisplayChatPreview(
    profileImage: String,
    username: String,
    messageSenderName: String,
    messageText: String,
    timestamp: String,
    onClick:  () -> Unit
) {

    Row(
        modifier = Modifier
            .padding(15.dp)
            .clickable(true) {
                onClick()
            },
    ) {
        ProfilePicture(
            modifier = Modifier
                .align(Alignment.Top)
                .offset(y = 2.dp),
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
                        .weight(1f),
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Text(
                    timestamp,
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 0.dp)
                        .align(Alignment.Top),
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
                    "$messageSenderName: $messageText",
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 4.dp),
                    textAlign = TextAlign.Left,
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}