package xyz.cisnux.mentoring.www.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import xyz.cisnux.mentoring.www.data.collections.Chat

class ChatDataSourceImpl(
    private val db: CoroutineDatabase
) : ChatDataSource {
    override suspend fun insertChatMessage(chat: Chat): Unit = withContext(Dispatchers.IO) {
        db.getCollection<Chat>(CHAT).insertOne(chat)
    }

    override suspend fun findAllChatMessagesById(roomChatId: String): List<Chat> = withContext(Dispatchers.IO){
        db.getCollection<Chat>(CHAT).find(Chat::roomChatId eq roomChatId).toList()
    }

    companion object {
        private const val CHAT = "chat"
    }
}