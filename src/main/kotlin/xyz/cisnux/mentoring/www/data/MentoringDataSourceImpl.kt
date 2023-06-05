package xyz.cisnux.mentoring.www.data

import kotlinx.coroutines.*
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.gt
import org.litote.kmongo.or
import xyz.cisnux.mentoring.www.data.collections.DetailMentoringSession
import xyz.cisnux.mentoring.www.data.collections.Mentoring

class MentoringDataSourceImpl(
    private val db: CoroutineDatabase
) : MentoringDataSource {

    override suspend fun insertMentoringSession(mentoring: DetailMentoringSession): String? =
        withContext(Dispatchers.IO) {
            db.getCollection<DetailMentoringSession>(MENTORING_SESSION).insertOne(
                mentoring
            ).insertedId?.toString()
        }


    override suspend fun updateMentoringSession(mentoring: DetailMentoringSession): String? =
        withContext(Dispatchers.IO) {
            db.getCollection<DetailMentoringSession>(MENTORING_SESSION).updateOne(
                DetailMentoringSession::id eq mentoring.id,
                mentoring
            ).upsertedId?.toString()
        }


    override suspend fun findAllMentoringSessionsById(userId: String): List<Mentoring> =
        withContext(Dispatchers.IO) {
            val currentTime = System.currentTimeMillis()
            db.getCollection<DetailMentoringSession>(MENTORING_SESSION).withDocumentClass<Mentoring>()
                .find(
                    or(DetailMentoringSession::mentorId eq userId, DetailMentoringSession::menteeId eq userId),
                    DetailMentoringSession::eventTime gt currentTime
                )
                .projection(
                    Mentoring::id,
                    Mentoring::title,
                    Mentoring::description,
                    Mentoring::isOnlyChat,
                    Mentoring::eventTime,
                )
                .toList()
        }


    override suspend fun findDetailMentoringSessionById(id: String): DetailMentoringSession? =
        withContext(Dispatchers.IO) {
            db.getCollection<DetailMentoringSession>(MENTORING_SESSION).findOneById(
                id
            )
        }

    companion object {
        private const val MENTORING_SESSION = "mentoringSession"
    }
}