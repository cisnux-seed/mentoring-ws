package xyz.cisnux.mentoring.www.models

data class MentoringNotificationContent(
    val mentoringId: String,
    val fullName: String,
    val photoProfile: String?,
    val title: String,
    val description: String,
    val deviceToken: String,
)
