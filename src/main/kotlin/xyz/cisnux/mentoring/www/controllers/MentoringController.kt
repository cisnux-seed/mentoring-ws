package xyz.cisnux.mentoring.www.controllers

import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import xyz.cisnux.mentoring.www.data.CloudMessagingDataSource
import xyz.cisnux.mentoring.www.data.MenteeDataSource
import xyz.cisnux.mentoring.www.data.MentoringDataSource
import xyz.cisnux.mentoring.www.data.collections.toGetMentoringSession
import xyz.cisnux.mentoring.www.models.*
import xyz.cisnux.mentoring.www.services.FirebaseCloudMessaging
import xyz.cisnux.mentoring.www.utils.AlreadyConnectionException
import java.util.concurrent.ConcurrentHashMap

class MentoringController(
    private val mentoringDataSource: MentoringDataSource,
    private val cloudMessagingDataSource: CloudMessagingDataSource,
    private val menteeDataSource: MenteeDataSource,
    private val firebaseCloudMessaging: FirebaseCloudMessaging,
) {
    private val userSockets = ConcurrentHashMap<String, WebSocketSession>()

    suspend fun onSubscribeMentoring(userId: String, socket: WebSocketSession) {
        if (userSockets.containsKey(userId)) {
            throw AlreadyConnectionException("connection already exist")
        }
        userSockets[userId] = socket
        val allMentoring = mentoringDataSource.findAllMentoringSessionsById(userId)
            .map {
                it.toGetMentoringSession()
            }
        val decodedAllMentoring = Json.encodeToString(allMentoring)
        userSockets[userId]?.send(Frame.Text(decodedAllMentoring))
    }

    suspend fun onCreateMentoringSession(addMentoringSession: AddMentoringSession) {
        val mentoringEntity = addMentoringSession.toMentoringSession()
        val mentoringId = mentoringDataSource.insertMentoringSession(mentoringEntity)
        val deviceToken = cloudMessagingDataSource.findCloudMessagingById(addMentoringSession.mentorId)?.deviceToken
        val mentee = menteeDataSource.findMenteeById(addMentoringSession.menteeId)
        val allMentoringForMentee = mentoringDataSource.findAllMentoringSessionsById(addMentoringSession.menteeId)
            .map {
                it.toGetMentoringSession()
            }
        val allMentoringForMentor = mentoringDataSource.findAllMentoringSessionsById(addMentoringSession.mentorId)
            .map {
                it.toGetMentoringSession()
            }
        val decodedAllMentoringForMentee = Json.encodeToString(allMentoringForMentee)
        val decodedAllMentoringForMentor = Json.encodeToString(allMentoringForMentor)

        userSockets[addMentoringSession.menteeId]?.send(Frame.Text(decodedAllMentoringForMentee))
        userSockets[addMentoringSession.mentorId]?.send(Frame.Text(decodedAllMentoringForMentor))

        if (mentoringId != null && deviceToken != null && mentee != null) {
            val mentoringNotificationContent = MentoringNotificationContent(
                mentoringId = mentoringId,
                fullName = mentee.fullName,
                title = addMentoringSession.title,
                description = addMentoringSession.description,
                photoProfile = mentee.photoProfile,
                deviceToken = deviceToken,
            )
            firebaseCloudMessaging.sendMentoringNotification(
                mentoringNotificationContent
            )
        }
    }
    fun tryDisconnect(userId: String) {
        userSockets[userId]
        if (userSockets.containsKey(userId)) {
            userSockets.remove(userId)
        }
    }
}
