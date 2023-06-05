package xyz.cisnux.mentoring.www.utils

data class AlreadyConnectionException
    (override val message: String) : Exception(message)