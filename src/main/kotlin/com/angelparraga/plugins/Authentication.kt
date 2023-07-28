package com.angelparraga.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.authentication() {
    install(Authentication) {
        basic {
            realm = "All"
            validate { credentials ->
                validate(credentials)
            }
        }
    }
}

private fun validate(credentials: UserPasswordCredential): Principal? {
    TODO("Create service to access db and check credentials")
}