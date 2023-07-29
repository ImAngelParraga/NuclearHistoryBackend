package com.angelparraga.plugins

import com.angelparraga.routes.ntApiRouting
import com.angelparraga.routes.nuclearRouting
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authenticate {
            ntApiRouting()
            nuclearRouting()
            get("/") {
                call.respondText("Hello World!")
            }
        }
    }
}
