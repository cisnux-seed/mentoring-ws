package xyz.cisnux.mentoring.www.models

import kotlinx.serialization.Serializable
import xyz.cisnux.mentoring.www.data.collections.Mentee

@Serializable
data class GetRoomMessages(
    val roomChatId: String,
    val mentor: Mentee,
    val mentee: Mentee,
    val endOfChatting: Long,
    val chats: List<GetChat>
)
