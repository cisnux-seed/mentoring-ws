package xyz.cisnux.mentoring.www.controllers

import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import xyz.cisnux.mentoring.www.data.ChatDataSource
import xyz.cisnux.mentoring.www.data.CloudMessagingDataSource
import xyz.cisnux.mentoring.www.data.MenteeDataSource
import xyz.cisnux.mentoring.www.data.RoomChatDataSource
import xyz.cisnux.mentoring.www.data.collections.toGetChat
import xyz.cisnux.mentoring.www.models.AddChat
import xyz.cisnux.mentoring.www.models.ChatNotificationContent
import xyz.cisnux.mentoring.www.models.GetRoomMessages
import xyz.cisnux.mentoring.www.models.toChat
import xyz.cisnux.mentoring.www.services.FirebaseCloudMessaging
import xyz.cisnux.mentoring.www.utils.AlreadyConnectionException
import java.util.concurrent.ConcurrentHashMap

class RoomChatController(
    private val roomChatDataSource: RoomChatDataSource,
    private val chatDataSource: ChatDataSource,
    private val cloudMessagingDataSource: CloudMessagingDataSource,
    private val menteeDataSource: MenteeDataSource,
    private val firebaseCloudMessaging: FirebaseCloudMessaging,
) {
    private val roomSockets = ConcurrentHashMap<String, WebSocketSession>()

    suspend fun onJoinRoomChat(userId: String, roomChatId: String, socket: WebSocketSession) {
        if (roomSockets.containsKey(userId)) {
            throw AlreadyConnectionException("connection already exist")
        }
        roomSockets[userId] = socket
        val room = roomChatDataSource.findRoomById(roomChatId)
        val chats = chatDataSource.findAllChatMessagesById(roomChatId)
        room?.let {
            val mentee = menteeDataSource.findMenteeById(room.menteeId)
            val mentor = menteeDataSource.findMenteeById(room.mentorId)
            if (mentee != null && mentor != null) {
                val getRoomMessages = GetRoomMessages(
                    roomChatId = room._id!!,
                    mentee = mentee,
                    mentor = mentor,
                    endOfChatting = room.endOfChatting,
                    chats = chats.map {
                        it.toGetChat()
                    }
                )
                val encodedRoomMessages = Json.encodeToString(getRoomMessages)
                roomSockets[userId]?.send(Frame.Text(encodedRoomMessages))
            }
        }
    }

    suspend fun onNewMessage(addChat: AddChat) {
        chatDataSource.insertChatMessage(addChat.toChat())
        val room = roomChatDataSource.findRoomById(addChat.roomChatId)
        val chats = chatDataSource.findAllChatMessagesById(addChat.roomChatId)
        room?.let {
            val mentee = menteeDataSource.findMenteeById(room.menteeId)
            val mentor = menteeDataSource.findMenteeById(room.mentorId)
            if (mentee != null && mentor != null){
                val getRoomMessages = GetRoomMessages(
                    roomChatId = room._id!!,
                    mentee = mentee,
                    mentor = mentor,
                    endOfChatting = room.endOfChatting,
                    chats = chats.map {
                        it.toGetChat()
                    }
                )
                val encodedRoomMessages = Json.encodeToString(getRoomMessages)
                roomSockets[addChat.senderId]?.send(Frame.Text(encodedRoomMessages))
                roomSockets[addChat.receiverId]?.send(Frame.Text(encodedRoomMessages))
            }

            val receiver = menteeDataSource.findMenteeById(addChat.receiverId)
            val deviceToken = cloudMessagingDataSource.findCloudMessagingById(addChat.receiverId)?.deviceToken
            if (receiver != null && deviceToken != null) {
                val chatNotificationContent = ChatNotificationContent(
                    roomChatId = addChat.roomChatId,
                    fullName = receiver.fullName,
                    email = receiver.email,
                    message = addChat.message,
                    photoProfile = receiver.photoProfile,
                    deviceToken = deviceToken,
                )
                firebaseCloudMessaging.sendChatNotification(chatNotificationContent)
            }
        }
    }

    fun tryDisconnect(userId: String) {
        roomSockets[userId]
        if (roomSockets.containsKey(userId)) {
            roomSockets.remove(userId)
        }
    }
}