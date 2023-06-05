package xyz.cisnux.mentoring.www.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import xyz.cisnux.mentoring.www.models.AddChat

class FirebaseCloudMessagingImpl(
    private val firebaseMessaging: FirebaseMessaging
) : FirebaseCloudMessaging {

    override suspend fun sendMentoringNotification(
        mentoringId: String,
        title: String,
        description: String,
        deviceToken: String,
    ) {
        val message = Message.builder()
            .putData("mentoringId", mentoringId)
            .putData("title", title)
            .putData("description", description)
            .setToken(deviceToken)
            .build()
        firebaseMessaging.send(message)
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