package xyz.cisnux.mentoring.www.services

import xyz.cisnux.mentoring.www.models.AddChat
import xyz.cisnux.mentoring.www.models.GetChat
import xyz.cisnux.mentoring.www.models.GetMentoringSession
import xyz.cisnux.mentoring.www.models.MentoringNotificationContent

interface FirebaseCloudMessaging {
    suspend fun sendMentoringNotification(
        mentoringNotificationContent: MentoringNotificationContent,
    )

    suspend fun sendChatNotification(
        addChat: AddChat,
        deviceToken: String
    )
}