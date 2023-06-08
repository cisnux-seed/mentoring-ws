package xyz.cisnux.mentoring.www.data

import com.mongodb.client.model.UpdateOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import xyz.cisnux.mentoring.www.data.collections.CloudMessaging

class CloudMessagingDataSourceImpl(
    private val db: CoroutineDatabase
) : CloudMessagingDataSource {
    override suspend fun upsertMessagingToken(cloudMessaging: CloudMessaging): String? =
        withContext(Dispatchers.IO) {
            cloudMessaging.apply {
                db.getCollection<CloudMessaging>(CLOUD_MESSAGING)
                    .updateOne(
                        CloudMessaging::userId eq cloudMessaging.userId, cloudMessaging,
                        UpdateOptions().upsert(true)
                    )
            }._id
        }

    override suspend fun findCloudMessagingById(userId: String): CloudMessaging? =
        withContext(Dispatchers.IO) {
            db.getCollection<CloudMessaging>(CLOUD_MESSAGING).findOne(
                CloudMessaging::userId eq userId
            )
        }


    companion object {
        private const val CLOUD_MESSAGING = "cloudMessaging"
    }
}