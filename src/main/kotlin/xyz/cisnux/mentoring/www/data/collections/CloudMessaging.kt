package xyz.cisnux.mentoring.www.data.collections

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class CloudMessaging(
    val id: String = ObjectId().toString(),
    val userId: String,
    val deviceToken: String
)
