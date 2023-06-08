package xyz.cisnux.mentoring.www.data.collections

import kotlinx.serialization.Serializable
import xyz.cisnux.mentoring.www.models.GetChat

@Serializable
data class Chat(
    val _id: String? = null,
    val roomChatId: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val createdAt: Long = System.currentTimeMillis()
)

fun Chat.toGetChat() = GetChat(
    id = _id!!,
    roomChatId, senderId, receiverId, message, createdAt
)