package xyz.cisnux.mentoring.www.di

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import xyz.cisnux.mentoring.www.controllers.CloudMessagingController
import xyz.cisnux.mentoring.www.controllers.DetailMentoringController
import xyz.cisnux.mentoring.www.controllers.MentoringController
import xyz.cisnux.mentoring.www.controllers.RoomChatController
import xyz.cisnux.mentoring.www.data.*
import xyz.cisnux.mentoring.www.services.FirebaseCloudMessaging
import xyz.cisnux.mentoring.www.services.FirebaseCloudMessagingImpl
import xyz.cisnux.mentoring.www.services.GoogleCalenderApiService
import xyz.cisnux.mentoring.www.services.GoogleCalenderApiServiceImpl
import java.io.File
import java.io.FileInputStream

private val service = CoroutineScope(Dispatchers.IO)
val mainModule = module {
    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("mentoring")
    }
    single<MentoringDataSource> {
        MentoringDataSourceImpl(get())
    }
    single<MenteeDataSource> {
        MenteeDataSourceImpl(get())
    }
    single<RoomChatDataSource> {
        RoomChatDataSourceImpl(get())
    }
    single<ChatDataSource> {
        ChatDataSourceImpl(get())
    }
    single<GoogleCalenderApiService> {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val jsonFactory = GsonFactory.getDefaultInstance()

        val calendarService = Calendar.Builder(httpTransport, jsonFactory, null)
            .setApplicationName("Dicoding Mentoring App")
            .build()

        GoogleCalenderApiServiceImpl(calendarService)
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
        MentoringController(
            get(),
            get(),
            get(),
            get()
        )
    }
    single {
        DetailMentoringController(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single {
        RoomChatController(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single {
        CloudMessagingController(get())
    }
}