package com.angelparraga.services.db

import com.angelparraga.services.db.DatabaseFactory.dbQuery
import com.angelparraga.services.db.domain.StatsOverview
import com.angelparraga.services.db.domain.User
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.firstOrNull


interface UserDAO {
    suspend fun getUser(steamId: String): User?
    suspend fun addUser(user: User): String
    suspend fun updateStatsOverview(steamId: String, statsOverview: StatsOverview): String
    suspend fun increaseCharacterRuns(steamId: String, character: String)
}

class UserDAOImpl : UserDAO {
    private val userCollection = DatabaseFactory.dbNTHistory.getCollection<User>("users")

    override suspend fun getUser(steamId: String): User? = dbQuery {
        userCollection.find(eq(User::steamId.name, steamId)).firstOrNull()
    }

    override suspend fun addUser(user: User): String = dbQuery {
        userCollection.insertOne(user).insertedId!!.asObjectId().value.toString()
    }

    override suspend fun updateStatsOverview(steamId: String, statsOverview: StatsOverview): String = dbQuery {
        val filter = eq(User::steamId.name, steamId)
        val update = Updates.set(User::statsOverview.name, statsOverview)
        userCollection.updateOne(filter, update).upsertedId!!.asObjectId().value.toString()
    }

    override suspend fun increaseCharacterRuns(steamId: String, character: String) = dbQuery {
        var user = getUser(steamId)
        if (user == null) {
            user = User(steamId)
            addUser(user)
        }

        val characters = user.characters.toMutableMap()
        characters[character] = (characters[character] ?: 0) + 1

        val filter = eq(User::steamId.name, steamId)
        val update = Updates.set(User::characters.name, characters)
        userCollection.updateOne(filter, update)

        Unit
    }
}