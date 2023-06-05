package xyz.cisnux.mentoring.www.data.collections

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import xyz.cisnux.mentoring.www.models.GetChat

@Serializable
data class Chat(
    val id: String = ObjectId().toString(),
    val roomChatId: String,
    val sender: String,
    val receiver: String,
    val message: String
)

fun Chat.toGetChat() = GetChat(
    roomChatId = roomChatId,
    sender = sender,
    receiver = receiver,
    message = message
)