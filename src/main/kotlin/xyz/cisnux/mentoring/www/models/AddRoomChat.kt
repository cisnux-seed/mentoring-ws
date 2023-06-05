package xyz.cisnux.mentoring.www.models

import kotlinx.serialization.Serializable
import xyz.cisnux.mentoring.www.data.collections.RoomChat

@Serializable
data class AddRoomChat(
    val mentorId: String,
    val menteeId: String,
    val duration: Int,
)
