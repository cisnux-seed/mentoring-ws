package xyz.cisnux.mentoring.www.data

import xyz.cisnux.mentoring.www.data.collections.CloudMessaging

interface CloudMessagingDataSource {
    suspend fun upsertMessagingToken(cloudMessaging: CloudMessaging): String?
    suspend fun findCloudMessagingById(userId: String): CloudMessaging?
}