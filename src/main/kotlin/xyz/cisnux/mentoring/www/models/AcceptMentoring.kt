package xyz.cisnux.mentoring.www.models

import kotlinx.serialization.Serializable

@Serializable
data class AcceptMentoring(
    val mentoringId: String,
    val isAccepted: Boolean,
    val duration: Int,
)
