package xyz.cisnux.mentoring.www.models

import kotlinx.serialization.Serializable

@Serializable
data class GetChat(
    val id: String,
    val roomChatId: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val createdAt: Long
)
