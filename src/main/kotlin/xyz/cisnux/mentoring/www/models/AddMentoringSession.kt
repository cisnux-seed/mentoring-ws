package xyz.cisnux.mentoring.www.models

import kotlinx.serialization.Serializable
import xyz.cisnux.mentoring.www.data.entities.MentoringSessionEntity

@Serializable
data class AddMentoringSession(
    val mentorId: String,
    val menteeId: String,
    val title: String,
    val description: String,
    val mentoringDate: Long,
    val mentoringTime: Long,
    val isOnlyChat: Boolean,
)

fun AddMentoringSession.toMentoringSessionEntity() = MentoringSessionEntity(
    mentorId = mentorId,
    menteeId = menteeId,
    title = title,
    description = description,
    mentoringDate = mentoringDate,
    mentoringTime = mentoringTime,
    isOnlyChat = isOnlyChat
)
