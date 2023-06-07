package xyz.cisnux.mentoring.www.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import org.koin.java.KoinJavaComponent.inject
import xyz.cisnux.mentoring.www.controllers.CloudMessagingController
import xyz.cisnux.mentoring.www.controllers.DetailMentoringController
import xyz.cisnux.mentoring.www.controllers.MentoringController
import xyz.cisnux.mentoring.www.routes.mentoringSocket
import xyz.cisnux.mentoring.www.routes.putDeviceToken

fun Application.configureRouting() {
    val cloudMessagingToken by inject<CloudMessagingController>(CloudMessagingController::class.java)
    val mentoringController by inject<MentoringController>(MentoringController::class.java)
    val detailMentoringController by inject<DetailMentoringController>(DetailMentoringController::class.java)
    install(Routing) {
        mentoringSocket(
            mentoringController = mentoringController,
            detailMentoringController = detailMentoringController
        )
        putDeviceToken(cloudMessagingToken)
    }
}
