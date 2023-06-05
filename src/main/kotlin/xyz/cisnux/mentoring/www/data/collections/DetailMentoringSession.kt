package xyz.cisnux.mentoring.www.data.collections

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import xyz.cisnux.mentoring.www.models.GetMentoringSession

@Serializable
data class DetailMentoringSession(
    val id: String = ObjectId().toString(),
    val mentorId: String,
    val menteeId: String,
    val title: String,
    val description: String,
    val eventTime: Long,
    val isOnlyChat: Boolean,
    val chatRoomId: String? = null,
    val videoChatLink: String? = null,
    val isCompleted: Boolean = false,
    val isAccepted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
data class Mentoring(
    val id: String,
    val title: String,
    val description: String,
    val isOnlyChat: Boolean,
    val eventTime: Long,
)

fun Mentoring.toGetMentoringSession() = GetMentoringSession(
    id, title, description, isOnlyChat, eventTime
)