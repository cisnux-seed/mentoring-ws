package xyz.cisnux.mentoring.www.data.collections

import kotlinx.serialization.Serializable
import xyz.cisnux.mentoring.www.models.GetMentoringSession

@Serializable
data class DetailMentoringSession(
    val _id: String? = null,
    val mentorId: String,
    val menteeId: String,
    val title: String,
    val description: String,
    val eventTime: Long,
    val isOnlyChat: Boolean,
    val roomChatId: String? = null,
    val videoChatLink: String? = null,
    val isCompleted: Boolean = false,
    val isAccepted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
data class Mentoring(
    val _id: String? = null,
    val title: String,
    val description: String,
    val isOnlyChat: Boolean,
    val eventTime: Long,
)

fun Mentoring.toGetMentoringSession() = GetMentoringSession(
    _id, title, description, isOnlyChat, eventTime
)