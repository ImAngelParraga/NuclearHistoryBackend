package com.angelparraga.services.db

import com.angelparraga.NuclearRunDB
import io.andrewohara.dynamokt.DataClassTableSchema
import io.andrewohara.dynamokt.createTableWithIndices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient
import software.amazon.awssdk.enhanced.dynamodb.Expression
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

interface DBService {
    suspend fun getNuclearRun(id: String): NuclearRunDB?
    suspend fun addNuclearRun(run: NuclearRunDB)
    suspend fun getAllNuclearRuns(steamId: String): List<NuclearRunDB>
    suspend fun getBySteamIdAndTimestamp(steamId: String, timestamp: Long): NuclearRunDB?
}

class DBServiceImpl : DBService {
    private val client = getTable()

    override suspend fun addNuclearRun(run: NuclearRunDB) {
        withContext(Dispatchers.IO) {
            println("Adding run: $run")
            client.putItem(run).get()
        }
    }

    override suspend fun getAllNuclearRuns(steamId: String): List<NuclearRunDB> = withContext(Dispatchers.IO) {
        val list = mutableListOf<NuclearRunDB>()

        val queryRequest = ScanEnhancedRequest.builder()
            .filterExpression(
                Expression.builder()
                    .expression("steamId = :steamId")
                    .putExpressionValue(":steamId", AttributeValue.builder().s(steamId).build())
                    .build()
            )
            .build()

        client.scan(queryRequest).items().subscribe { list.add(it) }.get()
        list
    }

    override suspend fun getBySteamIdAndTimestamp(steamId: String, timestamp: Long): NuclearRunDB? =
        withContext(Dispatchers.IO) {
            val queryRequest = ScanEnhancedRequest.builder()
                .filterExpression(
                    Expression.builder()
                        .expression("${NuclearRunDB::steamId.name} = :steamId AND ${NuclearRunDB::runTimestamp.name} = :runTimestamp")
                        .expressionValues(
                            mapOf(
                                ":steamId" to AttributeValue.builder().s(steamId).build(),
                                ":runTimestamp" to AttributeValue.builder().n(timestamp.toString()).build()
                            )
                        )
                        .build()
                )
                .build()

            var result: NuclearRunDB? = null
            client.scan(queryRequest).items().subscribe {
                println("Run: $it")
                result = it
            }.get()

            result
        }


    override suspend fun getNuclearRun(id: String): NuclearRunDB? = withContext(Dispatchers.IO) {
        client.getItem(keyFromId(id)).get()
    }

    private fun getTable(): DynamoDbAsyncTable<NuclearRunDB> {
        val client = DynamoDbAsyncClient.builder().region(DYNAMODB_REGION)
            .endpointOverride(java.net.URI.create(AWS_ENDPOINT_URL)).build()

        val schema = DataClassTableSchema(NuclearRunDB::class)
        val table = DynamoDbEnhancedAsyncClient.builder().dynamoDbClient(client).build().table("nuclear_runs", schema)

        table.createTableWithIndices()

        return table
    }
}