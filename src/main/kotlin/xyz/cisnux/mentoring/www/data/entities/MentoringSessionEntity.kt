package xyz.cisnux.mentoring.www.data.entities

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import xyz.cisnux.mentoring.www.models.GetMentoringSession

@Serializable
data class MentoringSessionEntity(
    val id: String = ObjectId().toString(),
    val mentorId: String,
    val menteeId: String,
    val title: String,
    val description: String,
    val mentoringDate: Long,
    val mentoringTime: Long,
    val isOnlyChat: Boolean,
    val isCompleted: Boolean = false,
    val isAccepted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
data class NotCompleteMentoring(
    val id: String,
    val title: String,
    val description: String,
    val isOnlyChat: Boolean,
    val mentoringDate: Long,
    val mentoringTime: Long
)

fun NotCompleteMentoring.toGetMentoringSession() = GetMentoringSession(
    id, title, description,isOnlyChat, mentoringDate, mentoringTime
)