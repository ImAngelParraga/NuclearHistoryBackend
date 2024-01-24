package com.angelparraga.plugins

import com.angelparraga.routes.ntApiRouting
import com.angelparraga.routes.nuclearRouting
import com.angelparraga.routes.statsRouting
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authenticate {
            ntApiRouting()
            nuclearRouting()
            statsRouting()
            get("/") {
                call.respondText("Hello World!")
            }
        }
    }
}
