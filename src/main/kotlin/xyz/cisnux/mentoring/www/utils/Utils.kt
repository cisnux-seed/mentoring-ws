package xyz.cisnux.mentoring.www.utils

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class Response<out T>(
    val statusCode: Int,
    val data: T
)