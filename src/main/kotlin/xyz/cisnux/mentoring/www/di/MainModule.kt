package xyz.cisnux.mentoring.www.di

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import xyz.cisnux.mentoring.www.controllers.CloudMessagingController
import xyz.cisnux.mentoring.www.controllers.MentoringController
import xyz.cisnux.mentoring.www.data.CloudMessagingDataSource
import xyz.cisnux.mentoring.www.data.CloudMessagingDataSourceImpl
import xyz.cisnux.mentoring.www.data.MentoringDataSource
import xyz.cisnux.mentoring.www.data.MentoringDataSourceImpl
import xyz.cisnux.mentoring.www.services.FirebaseCloudMessaging
import xyz.cisnux.mentoring.www.services.FirebaseCloudMessagingImpl

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
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .build()
        val firebaseApp = FirebaseApp.initializeApp(options)
        FirebaseMessaging.getInstance(firebaseApp)
    }
    single<FirebaseCloudMessaging> {
        FirebaseCloudMessagingImpl(get())
    }
    single<CloudMessagingDataSource> {
        CloudMessagingDataSourceImpl(get())
    }
    single {
        MentoringController(get(), get(), get())
    }
    single {
        CloudMessagingController(get())
    }
}