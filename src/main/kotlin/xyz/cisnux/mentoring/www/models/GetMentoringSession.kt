package xyz.cisnux.mentoring.www.models

import kotlinx.serialization.Serializable

@Serializable
data class GetMentoringSession(
    val id: String,
    val title: String,
    val description: String,
    val isOnlyChat: Boolean,
    val mentoringDate: Long,
    val mentoringTime: Long
)