package xyz.cisnux.mentoring.www.services

import xyz.cisnux.mentoring.www.models.ChatNotificationContent
import xyz.cisnux.mentoring.www.models.MentoringNotificationContent

interface FirebaseCloudMessaging {
    suspend fun sendMentoringNotification(
        mentoringNotificationContent: MentoringNotificationContent,
    )

    suspend fun sendChatNotification(
        chatNotificationContent: ChatNotificationContent
    )
}