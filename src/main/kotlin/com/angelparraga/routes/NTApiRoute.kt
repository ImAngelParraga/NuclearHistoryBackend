package com.angelparraga.routes

import com.angelparraga.*
import com.angelparraga.services.nuclear.NuclearService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.ntApiRouting() {
    val nuclearService: NuclearService by inject()

    route("/api") {
        get("/save-last-run") {
            try {
                val (steamId, key) = getParams(call)
                nuclearService.saveLastRun(steamId, key)
                call.respond(HttpStatusCode.OK, "Success")
            } catch (e: Exception) {
                when (e) {
                    is NuclearError.NoSteamIdProvided -> call.respond(HttpStatusCode.BadRequest, e.message)
                    is NuclearError.NoKeyProvided -> call.respond(HttpStatusCode.BadRequest, e.message)
                    is NuclearError.NoRunFound -> call.respond(HttpStatusCode.NotFound, e.message)
                    is NuclearError.RunAlreadyExists -> call.respond(HttpStatusCode.Conflict, e.message)
                    else -> call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown error.")
                }
            }
        }

        get("/current") {
            try {
                val (steamId, key) = getParams(call)
                val run = nuclearService.getCurrentRun(steamId, key)
                call.respond(HttpStatusCode.OK, run)
            } catch (e: Exception) {
                when (e) {
                    is NuclearError.NoSteamIdProvided -> call.respond(HttpStatusCode.BadRequest, e.message)
                    is NuclearError.NoKeyProvided -> call.respond(HttpStatusCode.BadRequest, e.message)
                    is NuclearError.NoRunFound -> call.respond(HttpStatusCode.NotFound, e.message)
                    else -> call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown error.")
                }
            }
        }

        get("/previous") {
            try {
                val (steamId, key) = getParams(call)
                val run = nuclearService.getPreviousRun(steamId, key)
                call.respond(HttpStatusCode.OK, run)
            } catch (e: Exception) {
                when (e) {
                    is NuclearError.NoSteamIdProvided -> call.respond(HttpStatusCode.BadRequest, e.message)
                    is NuclearError.NoKeyProvided -> call.respond(HttpStatusCode.BadRequest, e.message)
                    is NuclearError.NoRunFound -> call.respond(HttpStatusCode.NotFound, e.message)
                    else -> call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown error.")
                }
            }
        }
    }
}

private fun getParams(call: ApplicationCall): Pair<String, String> {
    val steamId = call.parameters["steamid"] ?: throw NuclearError.NoSteamIdProvided()
    val key = call.parameters["key"] ?: throw NuclearError.NoKeyProvided()
    return Pair(steamId, key)
}
