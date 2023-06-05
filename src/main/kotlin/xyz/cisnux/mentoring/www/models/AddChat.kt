package xyz.cisnux.mentoring.www.models

import kotlinx.serialization.Serializable

@Serializable
data class AddChat(
    val roomChatId: String,
    val sender: String,
    val receiver: String,
    val message: String,
)
