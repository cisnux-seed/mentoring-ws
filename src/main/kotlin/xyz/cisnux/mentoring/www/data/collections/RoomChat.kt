package xyz.cisnux.mentoring.www.data.collections

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class RoomChat(
    val id: String = ObjectId().toString(),
    val mentorId: String,
    val menteeId: String,
    val endOfChatting: Int,
)
