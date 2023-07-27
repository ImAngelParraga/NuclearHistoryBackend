package com.angelparraga.services.db

import com.angelparraga.NTRunDto
import com.angelparraga.NuclearRunDB
import com.angelparraga.services.db.DatabaseFactory.dbNTHistory
import com.angelparraga.services.db.DatabaseFactory.dbQuery
import com.angelparraga.toNTRunDto
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

interface NuclearRunDAO {
    suspend fun findById(id: String): NTRunDto?
    suspend fun addNuclearRun(run: NuclearRunDB)
    suspend fun findAll(steamId: String): List<NuclearRunDB>
    suspend fun findBySteamIdAndTimestamp(steamId: String, timestamp: Long): NuclearRunDB?
}

class MongoNuclearRunDAO : NuclearRunDAO {
    private val runsCollection = dbNTHistory.getCollection<NuclearRunDB>("runs")
    override suspend fun findById(id: String): NTRunDto? = dbQuery {
        val nuclearRunDB = runsCollection.find(eq("_id", ObjectId(id))).firstOrNull()
        nuclearRunDB?.toNTRunDto()
    }

    override suspend fun addNuclearRun(run: NuclearRunDB) {
        dbQuery {
            runsCollection.insertOne(run)
        }
    }

    override suspend fun findAll(steamId: String): List<NuclearRunDB> = dbQuery {
        runsCollection.find(eq(NuclearRunDB::steamId.name, steamId)).toList()
    }

    override suspend fun findBySteamIdAndTimestamp(steamId: String, timestamp: Long): NuclearRunDB? = dbQuery {
        val filter =
            Filters.and(eq(NuclearRunDB::runTimestamp.name, timestamp), eq(NuclearRunDB::steamId.name, steamId))
        runsCollection.find(filter).firstOrNull()
    }

}