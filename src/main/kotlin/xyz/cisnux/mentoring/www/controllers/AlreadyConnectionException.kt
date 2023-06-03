package xyz.cisnux.mentoring.www.controllers

data class AlreadyConnectionException
    (override val message: String) : Exception(message)