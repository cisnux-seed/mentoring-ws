package xyz.cisnux.mentoring.www.models

import kotlinx.serialization.Serializable
import xyz.cisnux.mentoring.www.data.collections.Chat

@Serializable
data class AddChat(
    val roomChatId: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val createdAt: Long = System.currentTimeMillis()
)

fun AddChat.toChat() = Chat(
    roomChatId = roomChatId,
    senderId = senderId,
    receiverId = receiverId,
    message = message,
    createdAt = createdAt
)