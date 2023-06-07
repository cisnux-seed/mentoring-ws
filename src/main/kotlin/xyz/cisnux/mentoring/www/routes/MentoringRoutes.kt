package xyz.cisnux.mentoring.www.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json
import xyz.cisnux.mentoring.www.controllers.DetailMentoringController
import xyz.cisnux.mentoring.www.utils.AlreadyConnectionException
import xyz.cisnux.mentoring.www.controllers.MentoringController
import xyz.cisnux.mentoring.www.models.AcceptMentoring
import xyz.cisnux.mentoring.www.models.AddMentoringSession

fun Route.mentoringSocket(
    mentoringController: MentoringController,
    detailMentoringController: DetailMentoringController,
) {
    webSocket("/mentoring") {
        val userId = call.parameters["userId"]
        try {
            if (userId != null) {
                mentoringController.onSubscribeMentoring(
                    userId = userId,
                    socket = this@webSocket
                )
            }
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val message = frame.readText()
                    val addMentoring = Json.decodeFromString<AddMentoringSession>(message)
                    mentoringController.onCreateMentoringSession(addMentoring)
                }
            }
        } catch (e: AlreadyConnectionException) {
            call.respond(HttpStatusCode.Conflict)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            userId?.let { mentoringController.tryDisconnect(it) }
        }
    }
    webSocket("/detailMentoring") {
        val userId = call.parameters["userId"]
        val mentoringId = call.parameters["mentoringId"]
        try {
            if (userId != null && mentoringId != null) {
                detailMentoringController.onSubscribeDetailMentoring(userId, mentoringId, this)
            }
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val message = frame.readText()
                    val acceptMentoring = Json.decodeFromString<AcceptMentoring>(message)
                    detailMentoringController.onAcceptedMentoringSession(acceptMentoring)
                }
            }
        } catch (e: AlreadyConnectionException) {
            call.respond(HttpStatusCode.Conflict)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            userId?.let { detailMentoringController.tryDisconnect(it) }
        }
    }
}