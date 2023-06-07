package xyz.cisnux.mentoring.www.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import xyz.cisnux.mentoring.www.data.collections.DetailMentee
import xyz.cisnux.mentoring.www.data.collections.Mentee

class MenteeDataSourceImpl(
    private val coroutineDatabase: CoroutineDatabase
) : MenteeDataSource {
    override suspend fun findMenteeById(id: String): Mentee? = withContext(Dispatchers.IO){
        coroutineDatabase.getCollection<DetailMentee>(MENTEE).withDocumentClass<Mentee>()
            .findOne(
                DetailMentee::id eq id
            )
    }

    companion object {
        private const val MENTEE = "mentees"
    }
}