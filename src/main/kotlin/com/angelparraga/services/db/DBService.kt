package com.angelparraga.services.db

import com.angelparraga.NuclearRunDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient

class DBService {
    private val client = getClient()

    suspend fun addNuclearRun(run: NuclearRunDB) {
        withContext(Dispatchers.IO) {
            println("Adding run: $run")
            client.putItem(run).get()
        }
    }

    suspend fun getNuclearRunDB(id: String): NuclearRunDB? = withContext(Dispatchers.IO) {
        client.getItem(keyFromId(id)).get()
    }

    private fun getClient(): DynamoDbAsyncTable<NuclearRunDB> {
        val client = DynamoDbAsyncClient.builder().region(DYNAMODB_REGION)
            .endpointOverride(java.net.URI.create(AWS_ENDPOINT_URL)).build()

        return DynamoDbEnhancedAsyncClient.builder().dynamoDbClient(client).build()
            .table(NUCLEAR_RUN_TABLE, tableSchema)
    }
}