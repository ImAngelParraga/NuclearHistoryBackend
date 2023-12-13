package com.angelparraga.routes

import com.angelparraga.NuclearError
import com.angelparraga.services.db.NuclearRunDAO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.nuclearRouting() {
    val nuclearRunDAO: NuclearRunDAO by inject()

    route("/nuclear") {
        get("/run") {
            try {
                val id = call.parameters["id"] ?: throw NuclearError.NoIdProvided()
                val run = nuclearRunDAO.findById(id) ?: throw NuclearError.NoRunFound()
                call.respond(HttpStatusCode.OK, run)
            } catch (e: Exception) {
                when (e) {
                    is NuclearError.NoIdProvided -> call.respond(HttpStatusCode.BadRequest, e.message)
                    is NuclearError.NoRunFound -> call.respond(HttpStatusCode.NotFound, e.message)
                    else -> call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown error.")
                }
            }
        }

        get("/runs") {
            try {
                val steamId = call.parameters["steamId"] ?: throw NuclearError.NoSteamIdProvided()
                val runs = nuclearRunDAO.findAll(steamId)
                call.respond(HttpStatusCode.OK, runs)
            } catch (e: Exception) {
                when (e) {
                    is NuclearError.NoSteamIdProvided -> call.respond(HttpStatusCode.BadRequest, e.message)
                    else -> call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown error.")
                }
            }
        }

        get("/runs/paginated") {
            try {
                val steamId = call.parameters["steamId"] ?: throw NuclearError.NoSteamIdProvided()
                val page = call.parameters["page"]?.toInt() ?: 0
                val pageSize = call.parameters["pageSize"]?.toInt() ?: 10
                val runs = nuclearRunDAO.findAllPaginated(steamId, page, pageSize)
                call.respond(HttpStatusCode.OK, runs)
            } catch (e: Exception) {
                when (e) {
                    is NuclearError.NoSteamIdProvided -> call.respond(HttpStatusCode.BadRequest, e.message)
                    else -> call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown error.")
                }
            }
        }
    }
}