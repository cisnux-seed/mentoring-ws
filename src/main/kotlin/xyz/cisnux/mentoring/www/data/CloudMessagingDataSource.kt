package xyz.cisnux.mentoring.www.data

import xyz.cisnux.mentoring.www.data.collections.CloudMessaging

interface CloudMessagingDataSource {
    suspend fun insertMessagingToken(cloudMessaging: CloudMessaging): String?
    suspend fun findCloudMessagingById(userId: String): CloudMessaging?
}