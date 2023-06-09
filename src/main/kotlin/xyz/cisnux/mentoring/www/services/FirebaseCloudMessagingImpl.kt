package xyz.cisnux.mentoring.www.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.cisnux.mentoring.www.models.ChatNotificationContent
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
                .putData("fullName", mentoringNotificationContent.fullName)
                .putData("photoProfile", mentoringNotificationContent.photoProfile)
                .putData("title", mentoringNotificationContent.title)
                .putData("description", mentoringNotificationContent.description)
                .setToken(mentoringNotificationContent.deviceToken)
                .build()
            firebaseMessaging.send(message)
        }
    }

    override suspend fun sendChatNotification(chatNotificationContent: ChatNotificationContent) {
        withContext(Dispatchers.IO) {
            val message = Message.builder()
                .putData("type", "chat")
                .putData("roomChatId", chatNotificationContent.roomChatId)
                .putData("fullName", chatNotificationContent.fullName)
                .putData("email", chatNotificationContent.email)
                .putData("message", chatNotificationContent.message)
                .putData("photoProfile", chatNotificationContent.photoProfile)
                .setToken(chatNotificationContent.deviceToken)
                .build()
            firebaseMessaging.send(message)
        }
    }
}