package xyz.cisnux.mentoring.www.data

import kotlinx.coroutines.*
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.or
import xyz.cisnux.mentoring.www.data.entities.MentoringSessionEntity
import xyz.cisnux.mentoring.www.data.entities.NotCompleteMentoring

class MentoringDataSourceImpl(
    private val db: CoroutineDatabase
) : MentoringDataSource {

    override suspend fun insertMentoring(mentoring: MentoringSessionEntity) {
        withContext(Dispatchers.IO) {
            db.getCollection<MentoringSessionEntity>().insertOne(
                mentoring
            )
        }
    }

    override suspend fun updateMentoring(mentoring: MentoringSessionEntity) {
        withContext(Dispatchers.IO) {
            db.getCollection<MentoringSessionEntity>().updateOne(
                MentoringSessionEntity::id eq mentoring.id,
                mentoring
            )
        }
    }

    override suspend fun getAllMentoringById(userId: String): List<NotCompleteMentoring> =
        withContext(Dispatchers.IO) {
            db.getCollection<MentoringSessionEntity>().withDocumentClass<NotCompleteMentoring>()
                .find(or(MentoringSessionEntity::mentorId eq userId, MentoringSessionEntity::menteeId eq userId))
                .projection(
                    NotCompleteMentoring::id,
                    NotCompleteMentoring::title,
                    NotCompleteMentoring::description,
                    NotCompleteMentoring::isOnlyChat,
                    NotCompleteMentoring::mentoringDate,
                    NotCompleteMentoring::mentoringTime
                )
                .toList()
        }


    override suspend fun getMentoringById(id: String): MentoringSessionEntity? =
        withContext(Dispatchers.IO) {
            db.getCollection<MentoringSessionEntity>().findOneById(
                id
            )
        }
}