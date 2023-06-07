package xyz.cisnux.mentoring.www.data.collections

import kotlinx.serialization.Serializable

@Serializable
data class RoomChat(
    val _id: String? = null,
    val mentorId: String,
    val menteeId: String,
    val endOfChatting: Long,
)
