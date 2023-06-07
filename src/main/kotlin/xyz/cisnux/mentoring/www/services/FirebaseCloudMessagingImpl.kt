package xyz.cisnux.mentoring.www.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.cisnux.mentoring.www.models.AddChat
import xyz.cisnux.mentoring.www.models.MentoringNotificationContent

class FirebaseCloudMessagingImpl(
    private val firebaseMessaging: FirebaseMessaging
) : FirebaseCloudMessaging {

    override suspend fun sendMentoringNotification(
        mentoringNotificationContent: MentoringNotificationContent
    ) {
        withContext(Dispatchers.IO) {
            val message = Message.builder()
                .putData("type", "mentoring")
                .putData("mentoringId", mentoringNotificationContent.mentoringId)
                .putData("menteeName", mentoringNotificationContent.fullName)
                .putData("menteePhotoProfile", mentoringNotificationContent.photoProfile)
                .putData("title", mentoringNotificationContent.title)
                .putData("description", mentoringNotificationContent.description)
                .setToken(mentoringNotificationContent.deviceToken)
                .build()
            firebaseMessaging.send(message)
        }
    }

    override suspend fun sendChatNotification(addChat: AddChat, deviceToken: String) {
        val message = Message.builder()
            .putData("roomChatId", addChat.roomChatId)
            .putData("sender", addChat.sender)
            .putData("message", addChat.message)
            .setToken(deviceToken)
            .build()
        firebaseMessaging.send(message)
    }
}