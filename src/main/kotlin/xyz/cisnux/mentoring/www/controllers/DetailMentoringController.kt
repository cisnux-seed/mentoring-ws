package xyz.cisnux.mentoring.www.controllers

import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import xyz.cisnux.mentoring.www.data.CloudMessagingDataSource
import xyz.cisnux.mentoring.www.data.MenteeDataSource
import xyz.cisnux.mentoring.www.data.MentoringDataSource
import xyz.cisnux.mentoring.www.data.RoomChatDataSource
import xyz.cisnux.mentoring.www.data.collections.RoomChat
import xyz.cisnux.mentoring.www.models.AcceptMentoring
import xyz.cisnux.mentoring.www.models.GetDetailMentoringSession
import xyz.cisnux.mentoring.www.models.MentoringNotificationContent
import xyz.cisnux.mentoring.www.services.FirebaseCloudMessaging
import xyz.cisnux.mentoring.www.services.GoogleCalenderApiService
import xyz.cisnux.mentoring.www.utils.AlreadyConnectionException
import xyz.cisnux.mentoring.www.utils.addDuration
import java.util.concurrent.ConcurrentHashMap

class DetailMentoringController(
    private val mentoringDataSource: MentoringDataSource,
    private val cloudMessagingDataSource: CloudMessagingDataSource,
    private val menteeDataSource: MenteeDataSource,
    private val roomChatDataSource: RoomChatDataSource,
    private val firebaseCloudMessaging: FirebaseCloudMessaging,
    private val googleCalenderApiService: GoogleCalenderApiService
) {
    private val detailMentoringSockets = ConcurrentHashMap<String, WebSocketSession>()

    suspend fun onSubscribeDetailMentoring(userId: String, mentoringId: String, socket: WebSocketSession) {
        if (detailMentoringSockets.containsKey(userId)) {
            throw AlreadyConnectionException("connection already exist")
        }
        detailMentoringSockets[userId] = socket
        val detailMentoring = mentoringDataSource.findDetailMentoringSessionById(mentoringId)
        detailMentoring?.let {
            val mentee = menteeDataSource.findMenteeById(detailMentoring.menteeId)
            val mentor = menteeDataSource.findMenteeById(detailMentoring.mentorId)
            println("mentee: $mentee")
            println("mentor: $mentor")
            if (mentee != null && mentor != null) {
                val encodedDetailMentoring = Json.encodeToString(
                    GetDetailMentoringSession(
                        id = detailMentoring._id,
                        mentor = mentor,
                        mentee = mentee,
                        title = detailMentoring.title,
                        description = detailMentoring.description,
                        eventTime = detailMentoring.eventTime,
                        isOnlyChat = detailMentoring.isOnlyChat,
                        isAccepted = detailMentoring.isAccepted,
                        isCompleted = detailMentoring.isCompleted,
                        roomChatId = detailMentoring.roomChatId,
                        videoChatLink = detailMentoring.videoChatLink
                    )
                )
                detailMentoringSockets[userId]?.send(Frame.Text(encodedDetailMentoring))
            }
        }
    }

    suspend fun onAcceptedMentoringSession(acceptedMentoring: AcceptMentoring) {
        val detailMentoring = mentoringDataSource.findDetailMentoringSessionById(acceptedMentoring.mentoringId)
        detailMentoring?.let {
            val mentee = menteeDataSource.findMenteeById(detailMentoring.menteeId)
            val mentor = menteeDataSource.findMenteeById(detailMentoring.mentorId)
            val deviceToken = cloudMessagingDataSource.findCloudMessagingById(detailMentoring.menteeId)?.deviceToken

            if (mentee != null && mentor != null) {
                val endOfMentoring = detailMentoring.eventTime.addDuration(acceptedMentoring.duration)
                val description = when {

                    acceptedMentoring.isAccepted && !detailMentoring.isOnlyChat -> {
                        val roomChat = RoomChat(
                            mentorId = detailMentoring.mentorId,
                            menteeId = detailMentoring.menteeId,
                            endOfChatting = endOfMentoring
                        )
                        val roomId = roomChatDataSource.insertRoomChat(roomChat)
                        val videoChatLink = googleCalenderApiService.addEvent(
                            title = detailMentoring.title,
                            description = detailMentoring.description,
                            menteeEmail = mentee.email,
                            mentorEmail = mentor.email,
                            endOfMentoring = endOfMentoring,
                            startOfMentoring = detailMentoring.eventTime
                        )
                        mentoringDataSource.updateMentoringSession(
                            detailMentoring.copy(
                                isAccepted = acceptedMentoring.isAccepted,
                                roomChatId = roomId,
                                videoChatLink = videoChatLink
                            )
                        )
                        "your mentoring session have been accepted by mentor"
                    }

                    acceptedMentoring.isAccepted -> {
                        val roomChat = RoomChat(
                            mentorId = detailMentoring.mentorId,
                            menteeId = detailMentoring.menteeId,
                            endOfChatting = endOfMentoring
                        )
                        val roomId = roomChatDataSource.insertRoomChat(roomChat)
                        mentoringDataSource.updateMentoringSession(
                            detailMentoring.copy(
                                isAccepted = acceptedMentoring.isAccepted,
                                roomChatId = roomId
                            )
                        )
                        "your mentoring session have been accepted by mentor"
                    }

                    else -> {
                        mentoringDataSource.updateMentoringSession(
                            detailMentoring.copy(
                                isAccepted = acceptedMentoring.isAccepted
                            )
                        )
                        "your mentorinng session have been rejected by mentor"
                    }
                }
                val latestDetailMentoring =
                    mentoringDataSource.findDetailMentoringSessionById(acceptedMentoring.mentoringId)
                latestDetailMentoring?.let {
                    val encodedDetailMentoring = Json.encodeToString(
                        GetDetailMentoringSession(
                            id = latestDetailMentoring._id,
                            mentor = mentor,
                            mentee = mentee,
                            title = latestDetailMentoring.title,
                            description = latestDetailMentoring.description,
                            eventTime = latestDetailMentoring.eventTime,
                            isOnlyChat = latestDetailMentoring.isOnlyChat,
                            isAccepted = latestDetailMentoring.isAccepted,
                            isCompleted = latestDetailMentoring.isCompleted,
                            roomChatId = latestDetailMentoring.roomChatId,
                            videoChatLink = latestDetailMentoring.videoChatLink
                        )
                    )
                    detailMentoringSockets[detailMentoring.menteeId]?.send(Frame.Text(encodedDetailMentoring))
                    detailMentoringSockets[detailMentoring.mentorId]?.send(Frame.Text(encodedDetailMentoring))
                }
                deviceToken?.let {
                    val mentoringNotificationContent = MentoringNotificationContent(
                        mentoringId = detailMentoring._id!!,
                        fullName = mentor.fullName,
                        title = detailMentoring.title,
                        description = description,
                        photoProfile = mentor.photoProfile,
                        deviceToken = deviceToken,
                    )
                    firebaseCloudMessaging.sendMentoringNotification(
                        mentoringNotificationContent
                    )
                }
            }
        }
    }

    fun tryDisconnect(userId: String) {
        detailMentoringSockets[userId]
        if (detailMentoringSockets.containsKey(userId)) {
            detailMentoringSockets.remove(userId)
        }
    }
}