package com.angelparraga.routes

import com.angelparraga.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.nuclearRouting() {
    route("/nuclear") {
        get {
            val ntResponse = callNuclearApiAndGetResponse()
            call.respond(HttpStatusCode.OK, ntResponse)
        }

        get("/current") {
            val ntResponse = callNuclearApiAndGetResponse()
            call.respond(HttpStatusCode.OK, ntResponse.current?.toDto() ?: "No current game")
        }

        get("/previous") {
            val ntResponse = callNuclearApiAndGetResponse()
            call.respond(HttpStatusCode.OK, ntResponse.previous?.toDto() ?: "No previous game")
        }
    }
}

private suspend fun callNuclearApiAndGetResponse(): NTResponse =
    HttpClient(CIO).use { client ->
        val response: String = client.get(BASE_URL) {
            url {
                parameters.append("s", MY_STEAMID)
                parameters.append("key", MY_KEY)
            }
        }.body()

        Json.decodeFromString(response)
    }