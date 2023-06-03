package xyz.cisnux.mentoring.www.di

import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import xyz.cisnux.mentoring.www.controllers.MentoringController
import xyz.cisnux.mentoring.www.data.MentoringDataSource
import xyz.cisnux.mentoring.www.data.MentoringDataSourceImpl

val mainModule = module {
    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("mentoring")
    }
    single<MentoringDataSource> {
        MentoringDataSourceImpl(get())
    }
    single {
        MentoringController(get())
    }
}