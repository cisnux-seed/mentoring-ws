package xyz.cisnux.mentoring.www.models

import kotlinx.serialization.Serializable

@Serializable
data class GetRoomMessages(
    val roomChatId: String,
    val mentorId: String,
    val menteeId: String,
    val endOfChatting: Long,
    val chats: List<GetChat>
)
