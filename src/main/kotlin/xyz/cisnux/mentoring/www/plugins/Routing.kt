package xyz.cisnux.mentoring.www.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import org.koin.java.KoinJavaComponent.inject
import xyz.cisnux.mentoring.www.controllers.MentoringController
import xyz.cisnux.mentoring.www.routes.mentoringSocket

fun Application.configureRouting() {
    val mentoringController by inject<MentoringController>(MentoringController::class.java)
    install(Routing) {
        mentoringSocket(mentoringController)
    }
}
