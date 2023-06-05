package xyz.cisnux.mentoring.www.services

import xyz.cisnux.mentoring.www.models.AddChat
import xyz.cisnux.mentoring.www.models.GetChat
import xyz.cisnux.mentoring.www.models.GetMentoringSession

interface FirebaseCloudMessaging {
    suspend fun sendMentoringNotification(
        mentoringId: String,
        title: String,
        description: String,
        deviceToken: String,
    )

    suspend fun sendChatNotification(
        addChat: AddChat,
        deviceToken: String
    )
}