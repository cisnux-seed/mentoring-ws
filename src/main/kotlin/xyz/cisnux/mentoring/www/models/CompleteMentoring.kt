package xyz.cisnux.mentoring.www.models

import kotlinx.serialization.Serializable

@Serializable
data class CompleteMentoring(
    val isCompleted: Boolean
)
