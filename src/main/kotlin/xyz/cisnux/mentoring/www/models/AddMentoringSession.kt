package xyz.cisnux.mentoring.www.models

import kotlinx.serialization.Serializable
import xyz.cisnux.mentoring.www.data.collections.DetailMentoringSession

@Serializable
data class AddMentoringSession(
    val mentorId: String,
    val menteeId: String,
    val title: String,
    val description: String,
    val eventTime: Long,
    val isOnlyChat: Boolean,
)

fun AddMentoringSession.toMentoringSession() = DetailMentoringSession(
    mentorId = mentorId,
    menteeId = menteeId,
    title = title,
    description = description,
    eventTime = eventTime,
    isOnlyChat = isOnlyChat
)
