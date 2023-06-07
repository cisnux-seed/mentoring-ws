package xyz.cisnux.mentoring.www.services

interface GoogleCalenderApiService {
    suspend fun addEvent(
        title: String,
        description: String,
        menteeEmail: String,
        mentorEmail: String,
        startOfMentoring: Long,
        endOfMentoring: Long
    ): String?
}