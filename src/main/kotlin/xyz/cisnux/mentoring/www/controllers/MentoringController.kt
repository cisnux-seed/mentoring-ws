package xyz.cisnux.mentoring.www.controllers

import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import xyz.cisnux.mentoring.www.data.MentoringDataSource
import xyz.cisnux.mentoring.www.data.entities.toGetMentoringSession
import xyz.cisnux.mentoring.www.models.AddMentoringSession
import xyz.cisnux.mentoring.www.models.toMentoringSessionEntity
import java.util.concurrent.ConcurrentHashMap

class MentoringController(
    private val mentoringDataSource: MentoringDataSource
) {
    private val userSockets = ConcurrentHashMap<String, WebSocketSession>()

    suspend fun onSubscribe(userId: String, socket: WebSocketSession) {
        if (userSockets.containsKey(userId)) {
            throw AlreadyConnectionException("connection already exist")
        }
        userSockets[userId] = socket
        val allMentoring = mentoringDataSource.getAllMentoringById(userId)
            .map {
                it.toGetMentoringSession()
            }
        val decodedAllMentoring = Json.encodeToString(allMentoring)
        userSockets[userId]?.send(Frame.Text(decodedAllMentoring))
    }

    suspend fun onCreateMentoringSession(addMentoringSession: AddMentoringSession) {
        val mentoringEntity = addMentoringSession.toMentoringSessionEntity()
        mentoringDataSource.insertMentoring(mentoringEntity)
        val allMentoringForMentee = mentoringDataSource.getAllMentoringById(addMentoringSession.menteeId)
            .map {
                it.toGetMentoringSession()
            }
        val allMentoringForMentor = mentoringDataSource.getAllMentoringById(addMentoringSession.mentorId)
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

    suspend fun tryDisconnect(userId: String) {
        userSockets[userId]
        if (userSockets.containsKey(userId)) {
            userSockets.remove(userId)
        }
    }
}