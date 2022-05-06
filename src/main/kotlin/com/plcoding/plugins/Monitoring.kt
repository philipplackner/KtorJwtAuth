package com.plcoding.plugins

import io.ktor.server.plugins.*
import org.slf4j.event.*
import io.ktor.server.request.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.response.*

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.DEBUG
        filter { call -> call.request.path().startsWith("/") }
    }

}
