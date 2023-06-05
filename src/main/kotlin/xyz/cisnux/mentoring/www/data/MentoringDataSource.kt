package xyz.cisnux.mentoring.www.data

import xyz.cisnux.mentoring.www.data.collections.DetailMentoringSession
import xyz.cisnux.mentoring.www.data.collections.Mentoring

interface MentoringDataSource {
    suspend fun insertMentoringSession(mentoring: DetailMentoringSession): String?
    suspend fun updateMentoringSession(mentoring: DetailMentoringSession): String?
    suspend fun findAllMentoringSessionsById(userId: String): List<Mentoring>
    suspend fun findDetailMentoringSessionById(id: String): DetailMentoringSession?
}