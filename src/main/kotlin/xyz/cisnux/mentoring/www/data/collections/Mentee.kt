package xyz.cisnux.mentoring.www.data.collections

import kotlinx.serialization.Serializable

@Serializable
data class DetailMentee(
    val id: String,
    val fullName: String,
    val username: String,
    val job: String,
    val email: String,
    val photoProfile: String?,
    val about: String
)

@Serializable
data class Mentee(
    val id: String,
    val fullName: String,
    val photoProfile: String?,
    val email: String
)
