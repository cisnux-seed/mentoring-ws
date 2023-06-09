package xyz.cisnux.mentoring.www.models

import kotlinx.serialization.Serializable

@Serializable
data class AddRoomChat(
    val mentorId: String,
    val menteeId: String,
    val duration: Int,
)
