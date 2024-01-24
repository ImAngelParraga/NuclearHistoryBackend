package com.angelparraga.services.nuclear

import com.angelparraga.*
import com.angelparraga.services.db.NuclearRunDAO
import com.angelparraga.services.db.UserDAO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

interface NuclearService {
    suspend fun getNTResponse(steamId: String, key: String): NTResponse?
    suspend fun saveLastRun(steamId: String, key: String): String
    suspend fun getCurrentRun(steamId: String, key: String): NuclearRunDB
    suspend fun getPreviousRun(steamId: String, key: String): NuclearRunDB
}

class NuclearServiceImpl(
    private val nuclearRunDAO: NuclearRunDAO,
    private val userDAO: UserDAO
) : NuclearService {
    override suspend fun getNTResponse(steamId: String, key: String): NTResponse? = withContext(Dispatchers.IO) {
        callNuclearApiAndGetResponse(steamId, key)
    }

    override suspend fun saveLastRun(steamId: String, key: String): String {
        val run =
            callNuclearApiAndGetResponse(steamId, key)?.previous?.toNuclearRunDB() ?: throw NuclearError.NoRunFound()

        println("Saving run: $run")
        if (!checkRunExists(run.steamId, run.runTimestamp)) {
            userDAO.increaseCharacterRuns(run.steamId, run.character)
            return nuclearRunDAO.addNuclearRun(run)
        } else {
            throw NuclearError.RunAlreadyExists()
        }
    }

    override suspend fun getCurrentRun(steamId: String, key: String): NuclearRunDB = withContext(Dispatchers.IO) {
        callNuclearApiAndGetResponse(steamId, key)?.current?.toNuclearRunDB() ?: throw NuclearError.NoRunFound()
    }

    override suspend fun getPreviousRun(steamId: String, key: String): NuclearRunDB = withContext(Dispatchers.IO) {
        callNuclearApiAndGetResponse(steamId, key)?.previous?.toNuclearRunDB() ?: throw NuclearError.NoRunFound()
    }

    private suspend fun checkRunExists(steamId: String, timestamp: Long): Boolean =
        nuclearRunDAO.findBySteamIdAndTimestamp(steamId, timestamp) != null


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
}