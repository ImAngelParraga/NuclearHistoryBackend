package com.angelparraga.routes

import com.angelparraga.*
import com.angelparraga.services.db.DBService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.Exception

fun Route.nuclearRouting() {
    route("/nuclear") {
        get("/{steamId}/{key}") {
            val steamId = call.parameters["steamId"] ?: throw Exception("No steamId provided.")
            val key = call.parameters["key"] ?: throw Exception("No key provided.")

            val ntResponse = callNuclearApiAndGetResponse(steamId, key)
            call.respondNullable(HttpStatusCode.OK, ntResponse)
        }

        get("/current/{steamId}/{key}") {
            val steamId = call.parameters["steamId"] ?: throw Exception("No steamId provided.")
            val key = call.parameters["key"] ?: throw Exception("No key provided.")

            val ntResponse = callNuclearApiAndGetResponse(steamId, key)
            call.respondNullable(HttpStatusCode.OK, ntResponse?.current?.toDto())
        }

        get("/previous/{steamId}/{key}") {
            val steamId = call.parameters["steamId"] ?: throw Exception("No steamId provided.")
            val key = call.parameters["key"] ?: throw Exception("No key provided.")

            val ntResponse = callNuclearApiAndGetResponse(steamId, key)
            call.respondNullable(HttpStatusCode.OK, ntResponse?.previous?.toDto())
        }

        get("/testrankis") {
            val ntResponse = callNuclearApiAndGetResponse(MY_STEAMID, MY_KEY)
            call.respondNullable(HttpStatusCode.OK, ntResponse?.previous?.toDto())
        }

        get("/testdata") {
            val data =
                "{\"current\":{\"char\":3,\"lasthit\":2,\"world\":1,\"level\":1,\"crown\":1,\"wepA\":43,\"wepB\":0,\"skin\":1,\"ultra\":0,\"charlvl\":2,\"loops\":0,\"win\":true,\"mutations\":\"00000000000000000000000000000\",\"kills\":15,\"health\":5,\"steamid\":76561198087280179,\"type\":\"normal\",\"timestamp\":1669801734},\"previous\":{\"char\":10,\"lasthit\":14,\"world\":3,\"level\":1,\"crown\":1,\"wepA\":4,\"wepB\":8,\"skin\":0,\"ultra\":0,\"charlvl\":5,\"loops\":0,\"win\":true,\"mutations\":\"00000001001100000000000000000\",\"kills\":173,\"health\":0,\"steamid\":76561198087280179,\"type\":\"normal\",\"timestamp\":1669800617}}"
            val response = Json.decodeFromString<NTResponse>(data)
            call.respond(HttpStatusCode.OK, response.toDto())
        }

        post("/testpost") {
            val data = call.receiveNullable<NTRun>() ?: throw Exception("No data provided.")
            val db = DBService()
            call.respondNullable(HttpStatusCode.OK, db.addNuclearRun(data.toNuclearRunDB()))
        }

        get("/testget") {
            val db = DBService()
            val id = call.parameters["id"] ?: throw Exception("No id provided.")
            db.getNuclearRunDB(id)
            call.respondNullable(HttpStatusCode.OK, "Se puso")
        }
    }
}

private suspend fun callNuclearApiAndGetResponse(steamId: String, key: String): NTResponse? =
    HttpClient(CIO).use { client ->
        val response: String = client.get(BASE_URL) {
            url {
                parameters.append("s", steamId)
                parameters.append("key", key)
            }
        }.body()

        val parsedResponse = Json.decodeFromString<NTResponse>(response)
        if (parsedResponse.current != null || parsedResponse.previous != null) {
            parsedResponse
        } else {
            null
        }
    }