package xyz.cisnux.mentoring.www.di

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import xyz.cisnux.mentoring.www.controllers.CloudMessagingController
import xyz.cisnux.mentoring.www.controllers.DetailMentoringController
import xyz.cisnux.mentoring.www.controllers.MentoringController
import xyz.cisnux.mentoring.www.data.*
import xyz.cisnux.mentoring.www.services.FirebaseCloudMessaging
import xyz.cisnux.mentoring.www.services.FirebaseCloudMessagingImpl
import xyz.cisnux.mentoring.www.services.GoogleCalenderApiService
import xyz.cisnux.mentoring.www.services.GoogleCalenderApiServiceImpl
import java.io.File
import java.io.FileReader


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
    single<GoogleCalenderApiService> {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val jsonFactory = GsonFactory.getDefaultInstance()

        val clientSecrets = GoogleClientSecrets.load(
            jsonFactory,
            File(
                "/Users/cisnux/ApplicationDevelopments/mentoring-ws/client_secret_4453505634-umfn9ppsp2v1kbs7fn5e6anpqf1smrkm.apps.googleusercontent.com.json"
            ).bufferedReader()
        )

        val flow = GoogleAuthorizationCodeFlow.Builder(
            httpTransport,
            jsonFactory,
            clientSecrets,
            listOf(CalendarScopes.CALENDAR)
        )
            .setDataStoreFactory(FileDataStoreFactory(File("/Users/cisnux/ApplicationDevelopments/mentoring-ws/")))
            .setAccessType("offline")
            .build()

        val receiver = LocalServerReceiver.Builder().setPort(8888).build()

        val credential = AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
        val calendar = Calendar.Builder(httpTransport, jsonFactory, credential)
            .setApplicationName("Dicoding Mentoring App")
            .build()
        GoogleCalenderApiServiceImpl(calendar)
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
        CloudMessagingController(get())
    }
}