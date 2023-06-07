package xyz.cisnux.mentoring.www.models

import kotlinx.serialization.Serializable
import xyz.cisnux.mentoring.www.data.collections.Mentee

@Serializable
data class GetDetailMentoringSession(
    val id: String?,
    val mentor: Mentee,
    val mentee: Mentee,
    val title: String,
    val description: String,
    val eventTime: Long,
    val isOnlyChat: Boolean,
    val roomChatId: String?,
    val videoChatLink: String?,
    val isCompleted: Boolean,
    val isAccepted: Boolean,
)