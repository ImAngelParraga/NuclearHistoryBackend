package com.angelparraga.services.db

import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseFactory {
    private val MONGODB_CONNECTION_URI = System.getenv("MONGODB_CONNECTION_URI")

    private val mongodbClient = MongoClient.create(MONGODB_CONNECTION_URI)
    val dbNTHistory = mongodbClient.getDatabase("NTHistoryDB")

    suspend fun <T> dbQuery(block: suspend () -> T): T = withContext(Dispatchers.IO) { block() }
}