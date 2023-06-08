package xyz.cisnux.mentoring.www.data.collections

import kotlinx.serialization.Serializable

@Serializable
data class CloudMessaging(
    val _id: String? = null,
    val userId: String,
    val deviceToken: String
)
