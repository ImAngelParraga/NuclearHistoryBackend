package com.angelparraga.services.db

import com.angelparraga.NTRunDto
import com.angelparraga.NuclearRunDB
import com.angelparraga.services.db.DatabaseFactory.dbNTHistory
import com.angelparraga.services.db.DatabaseFactory.dbQuery
import com.angelparraga.toNTRunDto
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

/**
 * Maybe this DAO shouldn't convert NuclearRunDB to NTRunDto, but I don't see the need to increase complexity when
 * these functions are called only in one place.
 */
interface NuclearRunDAO {
    suspend fun findById(id: String): NTRunDto?
    suspend fun addNuclearRun(run: NuclearRunDB): String
    suspend fun findAll(steamId: String): List<NTRunDto>
    suspend fun findAllPaginated(steamId: String, page: Int, pageSize: Int): List<NTRunDto>
    suspend fun findBySteamIdAndTimestamp(steamId: String, timestamp: Long): NuclearRunDB?
}

class MongoNuclearRunDAO : NuclearRunDAO {
    private val runsCollection = dbNTHistory.getCollection<NuclearRunDB>("runs")

    override suspend fun findById(id: String): NTRunDto? = dbQuery {
        val nuclearRunDB = runsCollection.find(eq("_id", ObjectId(id))).firstOrNull()
        nuclearRunDB?.toNTRunDto()
    }

    override suspend fun addNuclearRun(run: NuclearRunDB): String = dbQuery {
        runsCollection.insertOne(run).insertedId!!.asObjectId().value.toString()
    }

    override suspend fun findAll(steamId: String): List<NTRunDto> = dbQuery {
        runsCollection.find(eq(NuclearRunDB::steamId.name, steamId)).map { it.toNTRunDto() }.toList()
    }

    override suspend fun findAllPaginated(
        steamId: String,
        page: Int,
        pageSize: Int
    ): List<NTRunDto> = dbQuery {
        runsCollection
            .find(eq(NuclearRunDB::steamId.name, steamId))
            .skip(page * pageSize)
            .limit(pageSize)
            .map { it.toNTRunDto() }
            .toList()
    }

    override suspend fun findBySteamIdAndTimestamp(steamId: String, timestamp: Long): NuclearRunDB? = dbQuery {
        val filter =
            Filters.and(eq(NuclearRunDB::runTimestamp.name, timestamp), eq(NuclearRunDB::steamId.name, steamId))
        runsCollection.find(filter).firstOrNull()
    }

}