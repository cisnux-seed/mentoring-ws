package xyz.cisnux.mentoring.www.data

import xyz.cisnux.mentoring.www.data.collections.Chat

interface ChatDataSource {
    suspend fun insertChatMessage(chat: Chat)
    suspend fun findAllChatMessagesById(roomChatId: String): List<Chat>
}