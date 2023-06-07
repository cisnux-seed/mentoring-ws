package xyz.cisnux.mentoring.www.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.or
import xyz.cisnux.mentoring.www.data.collections.RoomChat

class RoomChatDataSourceImpl(
    private val db: CoroutineDatabase
) : RoomChatDataSource {
    override suspend fun insertRoomChat(roomChat: RoomChat): String? = withContext(Dispatchers.IO) {
        roomChat.apply {
            db.getCollection<RoomChat>(ROOM_CHAT).insertOne(roomChat)
        }._id
    }

    override suspend fun findAllRoomChatsById(userId: String): List<RoomChat> = withContext(Dispatchers.IO) {
        db.getCollection<RoomChat>(ROOM_CHAT).find(or(RoomChat::menteeId eq userId, RoomChat::mentorId eq userId))
            .toList()
    }

    override suspend fun findRoomById(id: String): RoomChat? = withContext(Dispatchers.IO) {
        db.getCollection<RoomChat>(ROOM_CHAT).findOneById(id)
    }

    companion object {
        private const val ROOM_CHAT = "roomChat"
    }
}