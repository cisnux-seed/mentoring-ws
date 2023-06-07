package xyz.cisnux.mentoring.www.data

import xyz.cisnux.mentoring.www.data.collections.Mentee

interface MenteeDataSource {
    suspend fun findMenteeById(id: String): Mentee?
}