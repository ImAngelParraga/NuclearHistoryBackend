package com.angelparraga

import com.angelparraga.plugins.authentication
import com.angelparraga.plugins.configureKoin
import com.angelparraga.plugins.configureRouting
import com.angelparraga.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureSerialization()
    configureKoin()
    authentication()
    configureRouting()
}

