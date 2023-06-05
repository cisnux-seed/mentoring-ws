package xyz.cisnux.mentoring.www.controllers

import xyz.cisnux.mentoring.www.data.CloudMessagingDataSource
import xyz.cisnux.mentoring.www.models.AddMessagingToken
import xyz.cisnux.mentoring.www.models.toCloudMessagingToken

class CloudMessagingController(
    private val cloudMessagingDataSource: CloudMessagingDataSource
) {
    suspend fun putToken(addMessagingToken: AddMessagingToken): String? =
        cloudMessagingDataSource.upsertMessagingToken(addMessagingToken.toCloudMessagingToken())
}