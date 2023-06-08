package xyz.cisnux.mentoring.www.services

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.*
import kotlinx.coroutines.CompletionHandlerException
import kotlinx.coroutines.InternalCoroutinesApi
import com.google.api.client.util.DateTime
import java.text.SimpleDateFormat
import java.util.*

class GoogleCalenderApiServiceImpl(
    private val calendarService: Calendar
) : GoogleCalenderApiService {
    @OptIn(InternalCoroutinesApi::class)
    override suspend fun addEvent(
        title: String,
        description: String,
        menteeEmail: String,
        mentorEmail: String,
        startOfMentoring: Long,
        endOfMentoring: Long
    ): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
        val event: Event = Event()
            .setSummary(title)
            .setLocation("Bandung, Jawa Barat")
            .setDescription(description)

        val start: EventDateTime = EventDateTime()
            .setDateTime(DateTime(dateFormat.format(Date(startOfMentoring))))
            .setTimeZone("Asia/Jakarta")
        event.start = start

        val end: EventDateTime = EventDateTime()
            .setDateTime(DateTime(dateFormat.format(Date(endOfMentoring))))
            .setTimeZone("Asia/Jakarta")
        event.end = end

        val attendees: ArrayList<EventAttendee> = arrayListOf(
            EventAttendee().setEmail(menteeEmail),
            EventAttendee().setEmail(mentorEmail)
        )
        event.attendees = attendees

        val reminderOverrides = arrayListOf<EventReminder>(
            EventReminder().setMethod("email").setMinutes(24 * 60),
            EventReminder().setMethod("popup").setMinutes(10)
        )
        val reminders = Event.Reminders()
            .setUseDefault(false)
            .setOverrides(reminderOverrides)
        event.reminders = reminders

        val calendarId = "primary"
        val conferenceData = ConferenceData()
        conferenceData.createRequest = CreateConferenceRequest()
            .setRequestId(System.currentTimeMillis().toString())
            .setConferenceSolutionKey(
                ConferenceSolutionKey().setType("hangoutsMeet")
            )
        event.conferenceData = conferenceData

        val linkMeet = try {
            val result = calendarService.events().insert(calendarId, event)
                .setKey("AIzaSyAx_02w9aCYWbhh-45a2xbyianDJ00eF_g")
                .setSendNotifications(true)
                .setConferenceDataVersion(1)
                .execute()
            println("calendar: ${result?.conferenceData?.entryPoints}")
            result?.conferenceData?.entryPoints?.first()?.let {
                it["uri"].toString()
            }
        } catch (e: CompletionHandlerException) {
            e.printStackTrace()
            null
        } catch (e: GoogleJsonResponseException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return linkMeet
    }
}