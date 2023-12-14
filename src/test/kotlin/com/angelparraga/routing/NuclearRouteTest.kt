package com.angelparraga.routing

import com.angelparraga.NTRunDto
import com.angelparraga.routes.nuclearRouting
import com.angelparraga.services.db.NuclearRunDAO
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import org.koin.dsl.module
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * I don't think this is the best approach for testing, but it works. This class was used to learn how to test.
 */
class NuclearRouteTest {

    private val nuclearRunDAOMock = mockk<NuclearRunDAO>()
    private val dummyNTRunDto = getDummyNTRunDto()

    private fun nuclearRouteTestApplication(block: suspend ApplicationTestBuilder.() -> Unit) =
        baseTestApplication(koinModule()) {
            routing {
                nuclearRouting()
            }

            block()
        }

    @Test
    fun testGetRun() = nuclearRouteTestApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        coEvery { nuclearRunDAOMock.findById("runId") } returns dummyNTRunDto

        val response = client.get("/nuclear/run") {
            parameter("id", "runId")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(response.body(), dummyNTRunDto)
    }

    @Test
    fun testGetAllRuns() = nuclearRouteTestApplication {
        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        coEvery { nuclearRunDAOMock.findAll("steamId") } returns listOf(dummyNTRunDto)

        val response = client.get("/nuclear/runs") {
            parameter("steamId", "steamId")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf(dummyNTRunDto), response.body())
    }

    private fun koinModule() = module {
        single { nuclearRunDAOMock }
    }

    private fun getDummyNTRunDto() = NTRunDto(
        runId = "runId",
        character = "character",
        lastHit = "lastHit",
        world = "world",
        worldLevel = 1,
        crown = "crown",
        weaponA = "weaponA",
        weaponB = "weaponB",
        skin = 'A',
        ultraMutation = "ultraMutation",
        characterLvl = 1,
        loops = 1,
        win = true,
        mutations = listOf("mutation"),
        kills = 1,
        health = 1,
        steamId = "steamId",
        type = "type",
        timestamp = Instant.now().toEpochMilli()
    )
}