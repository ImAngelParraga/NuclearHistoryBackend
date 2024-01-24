package com.angelparraga.routes

import com.angelparraga.NuclearError
import com.angelparraga.services.stats.StatsService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.statsRouting() {
    val statsService: StatsService by inject()

    route("/stats") {
        get("/overview") {
            try {
                val steamId = call.parameters["steamId"] ?: throw NuclearError.NoSteamIdProvided()
                val statsOverview = statsService.getStatsOverview(steamId)
                call.respond(HttpStatusCode.OK, statsOverview)
            } catch (e: Exception) {
                when (e) {
                    is NuclearError.NoSteamIdProvided -> call.respond(HttpStatusCode.BadRequest, e.message)
                    is NuclearError.UserNotFound -> call.respond(HttpStatusCode.NotFound, e.message)
                    is NuclearError.NoStatsOverview -> call.respond(HttpStatusCode.NotFound, e.message)
                }
            }
        }

        put("/overview") {
            try {
                val steamId = call.parameters["steamId"] ?: throw NuclearError.NoSteamIdProvided()
                val statsOverview = statsService.updateStatsOverview(steamId)
                call.respond(HttpStatusCode.OK, statsOverview)
            } catch (e: Exception) {
                when (e) {
                    is NuclearError.NoSteamIdProvided -> call.respond(HttpStatusCode.BadRequest, e.message)
                    is NuclearError.WorldNotFound -> call.respond(HttpStatusCode.NotFound, e.message)
                }
            }
        }
    }
}