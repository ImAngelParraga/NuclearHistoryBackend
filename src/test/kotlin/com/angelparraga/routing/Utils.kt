package com.angelparraga.routing

import com.angelparraga.plugins.configureSerialization
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

fun baseTestApplication(koinModule: Module, block: suspend ApplicationTestBuilder.() -> Unit) = testApplication {
    environment {
        config = MapApplicationConfig("ktor.environment" to "test")
    }

    application {
        configureSerialization()
    }

    startKoin {
        modules(koinModule)
    }

    block()

    stopKoin()
}