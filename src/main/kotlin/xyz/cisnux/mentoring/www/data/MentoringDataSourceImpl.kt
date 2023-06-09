package xyz.cisnux.mentoring.www.data

import kotlinx.coroutines.*
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase
import xyz.cisnux.mentoring.www.data.collections.DetailMentoringSession
import xyz.cisnux.mentoring.www.data.collections.Mentoring

class MentoringDataSourceImpl(
    private val db: CoroutineDatabase
) : MentoringDataSource {

    override suspend fun insertMentoringSession(mentoring: DetailMentoringSession): String? =
        withContext(Dispatchers.IO) {
            mentoring.apply {
                db.getCollection<DetailMentoringSession>(MENTORING_SESSION).insertOne(
                    mentoring
                )
            }._id
        }

    override suspend fun updateMentoringSession(
        detailMentoringSession: DetailMentoringSession,
    ): Unit = withContext(Dispatchers.IO) {
        detailMentoringSession._id?.let {
            db.getCollection<DetailMentoringSession>(MENTORING_SESSION)
                .updateOneById(
                    it,
                    detailMentoringSession
                )
        }
    }


    override suspend fun findAllMentoringSessionsById(userId: String): List<Mentoring> =
        withContext(Dispatchers.IO) {
            db.getCollection<DetailMentoringSession>(MENTORING_SESSION).withDocumentClass<Mentoring>()
                .find(
                    or(DetailMentoringSession::mentorId eq userId, DetailMentoringSession::menteeId eq userId)
                )
                .projection(
                    Mentoring::_id,
                    Mentoring::title,
                    Mentoring::description,
                    Mentoring::isOnlyChat,
                    Mentoring::eventTime,
                )
                .sort(descending(Mentoring::eventTime))
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