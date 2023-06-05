package xyz.cisnux.mentoring.www.controllers

import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import xyz.cisnux.mentoring.www.data.CloudMessagingDataSource
import xyz.cisnux.mentoring.www.data.MentoringDataSource
import xyz.cisnux.mentoring.www.data.collections.toGetMentoringSession
import xyz.cisnux.mentoring.www.models.AddMentoringSession
import xyz.cisnux.mentoring.www.models.toMentoringSession
import xyz.cisnux.mentoring.www.services.FirebaseCloudMessaging
import xyz.cisnux.mentoring.www.utils.AlreadyConnectionException
import java.util.concurrent.ConcurrentHashMap

class MentoringController(
    private val mentoringDataSource: MentoringDataSource,
    private val cloudMessagingDataSource: CloudMessagingDataSource,
    private val firebaseCloudMessaging: FirebaseCloudMessaging
) {
    private val userSockets = ConcurrentHashMap<String, WebSocketSession>()

    suspend fun onSubscribe(userId: String, socket: WebSocketSession) {
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
        if (mentoringId != null && deviceToken != null) {
            firebaseCloudMessaging.sendMentoringNotification(
                mentoringId = mentoringId,
                title = addMentoringSession.title,
                description = addMentoringSession.description,
                deviceToken = deviceToken
            )
        }

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
    }

    // this feature not implemented yet
    suspend fun onUpdateMentoringSession(addMentoringSession: AddMentoringSession) {
    }

    fun tryDisconnect(userId: String) {
        userSockets[userId]
        if (userSockets.containsKey(userId)) {
            userSockets.remove(userId)
        }
    }
}