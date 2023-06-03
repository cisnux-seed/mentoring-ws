package xyz.cisnux.mentoring.www

import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.koin.ktor.plugin.Koin
import xyz.cisnux.mentoring.www.di.mainModule

import xyz.cisnux.mentoring.www.plugins.*

fun main(args: Array<String>): Unit =
    EngineMain.main(args)

fun Application.module() {
    install(Koin){
        modules(mainModule)
    }
    configureSockets()
    configureSerialization()
    configureMonitoring()
    configureRouting()
}
