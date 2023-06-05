package xyz.cisnux.mentoring.www.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json
import xyz.cisnux.mentoring.www.utils.AlreadyConnectionException
import xyz.cisnux.mentoring.www.controllers.MentoringController
import xyz.cisnux.mentoring.www.models.AddMentoringSession

fun Route.mentoringSocket(mentoringController: MentoringController) {
    webSocket("/mentoring") {
        val userId = call.parameters["userId"]
        try {
            if (userId != null) {
                mentoringController.onSubscribe(
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
}