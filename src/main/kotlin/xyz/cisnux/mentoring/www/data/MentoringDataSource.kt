package xyz.cisnux.mentoring.www.data

import xyz.cisnux.mentoring.www.data.entities.MentoringSessionEntity
import xyz.cisnux.mentoring.www.data.entities.NotCompleteMentoring

interface MentoringDataSource {
    suspend fun insertMentoring(mentoring: MentoringSessionEntity)
    suspend fun updateMentoring(mentoring: MentoringSessionEntity)
    suspend fun getAllMentoringById(userId: String): List<NotCompleteMentoring>
    suspend fun getMentoringById(id: String): MentoringSessionEntity?
}