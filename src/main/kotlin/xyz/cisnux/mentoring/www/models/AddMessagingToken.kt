package xyz.cisnux.mentoring.www.models

import kotlinx.serialization.Serializable
import xyz.cisnux.mentoring.www.data.collections.CloudMessaging

@Serializable
data class AddMessagingToken(
    val userId: String,
    val deviceToken: String
)

fun AddMessagingToken.toCloudMessagingToken() =
    CloudMessaging(userId = userId, deviceToken = deviceToken)
