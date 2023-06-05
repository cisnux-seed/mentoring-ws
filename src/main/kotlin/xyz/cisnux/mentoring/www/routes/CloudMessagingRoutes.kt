package xyz.cisnux.mentoring.www.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import xyz.cisnux.mentoring.www.controllers.CloudMessagingController
import xyz.cisnux.mentoring.www.models.AddMessagingToken

fun Route.postDeviceToken(cloudMessagingController: CloudMessagingController) {
    post("/cloudMessaging") {
        val addMessagingToken = call.receive<AddMessagingToken>()
        cloudMessagingController.postToken(addMessagingToken)
        call.respondText("token stored correctly", status = HttpStatusCode.Created)
    }
}