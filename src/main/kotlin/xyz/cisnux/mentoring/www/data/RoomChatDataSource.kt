package xyz.cisnux.mentoring.www.data

import xyz.cisnux.mentoring.www.data.collections.RoomChat

interface RoomChatDataSource {
    suspend fun insertRoomChat(roomChat: RoomChat): String?
    suspend fun findAllRoomChatsById(userId: String): List<RoomChat>
    suspend fun findRoomById(id: String): RoomChat?
}