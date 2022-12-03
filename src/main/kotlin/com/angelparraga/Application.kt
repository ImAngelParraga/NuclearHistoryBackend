package com.angelparraga

import io.ktor.server.application.*
import com.angelparraga.plugins.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureSerialization()
    configureRouting()

    install(Koin) {
        modules(appModule)
    }
}

val appModule = module {

}