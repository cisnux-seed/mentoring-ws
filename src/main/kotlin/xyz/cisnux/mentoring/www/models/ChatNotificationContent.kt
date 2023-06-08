package xyz.cisnux.mentoring.www.models

data class ChatNotificationContent(
    val roomChatId: String,
    val fullName: String,
    val email: String,
    val message: String,
    val photoProfile: String?,
    val deviceToken: String,
)