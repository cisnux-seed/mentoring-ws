package xyz.cisnux.mentoring.www.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json
import xyz.cisnux.mentoring.www.utils.AlreadyConnectionException
import xyz.cisnux.mentoring.www.controllers.RoomChatController
import xyz.cisnux.mentoring.www.models.AddChat

fun Route.chatSocket(
    chatController: RoomChatController,
) {
    webSocket("/chat") {
        val userId = call.parameters["userId"]
        val roomChatId = call.parameters["roomChatId"]
        try {
            if (userId != null && roomChatId != null) {
                chatController.onJoinRoomChat(
                    userId = userId,
                    roomChatId = roomChatId,
                    socket = this@webSocket
                )
            }
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val message = frame.readText()
                    val newMessage = Json.decodeFromString<AddChat>(message)
                    chatController.onNewMessage(newMessage)
                }
            }
        } catch (e: AlreadyConnectionException) {
            call.respond(HttpStatusCode.Conflict)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            userId?.let { chatController.tryDisconnect(it) }
        }
    }
}