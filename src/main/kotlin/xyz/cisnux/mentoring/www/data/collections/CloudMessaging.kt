package xyz.cisnux.mentoring.www.data.collections

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class CloudMessaging(
    val _id: String? = null,
    val userId: String,
    val deviceToken: String
)
