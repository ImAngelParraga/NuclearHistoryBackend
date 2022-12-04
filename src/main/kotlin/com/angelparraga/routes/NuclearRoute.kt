package com.angelparraga.routes

import com.angelparraga.NuclearError
import com.angelparraga.services.db.DBService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.nuclearRouting() {
    val dbService: DBService by inject()

    route("/nuclear") {
        get("/getrun") {
            try {
                val id = call.parameters["id"] ?: throw NuclearError.NoIdProvided()
                val run = dbService.getNuclearRun(id) ?: throw NuclearError.NoRunFound()
                call.respond(HttpStatusCode.OK, run)
            } catch (e: Exception) {
                when (e) {
                    is NuclearError.NoIdProvided -> call.respond(HttpStatusCode.BadRequest, e.message)
                    is NuclearError.NoRunFound -> call.respond(HttpStatusCode.NotFound, e.message)
                    else -> call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown error.")
                }
            }
        }

        get("/getallforsteamid") {
            try {
                val steamId = call.parameters["steamId"] ?: throw NuclearError.NoSteamIdProvided()
                val runs = dbService.getAllNuclearRuns(steamId)
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