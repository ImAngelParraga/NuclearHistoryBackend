package com.angelparraga.services.db

import aws.sdk.kotlin.runtime.endpoint.AwsEndpoint
import aws.sdk.kotlin.runtime.endpoint.StaticEndpointResolver
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.sdk.kotlin.services.dynamodb.model.PutItemRequest
import com.angelparraga.*
import kotlin.reflect.full.declaredMemberProperties

class DBService {
    suspend fun addNuclearRun(run: NuclearRunDB) {
        val itemValues = mutableMapOf<String, AttributeValue>()
        itemValues.addContent(run)
        val request = PutItemRequest {
            tableName = NUCLEAR_RUN_TABLE
            item = itemValues
        }
        getClient().use { client ->
            client.putItem(request)
            println("Item added")
        }
    }

    suspend fun getNuclearRunDB(id: String) {
        val request = GetItemRequest {
            tableName = NUCLEAR_RUN_TABLE
            key = mapOf("id" to AttributeValue.S(id))
        }
        getClient().use { client ->
            val response = client.getItem(request)
            val item = response.item
            if (item != null) {
                item.forEach {
                    println("${it.key} = ${it.value}")
                }
                println("Item retrieved: $item")
            } else {
                println("No item found with the key $id")
            }
        }
    }

    private fun getClient(): DynamoDbClient {
        val endpoint = AwsEndpoint(AWS_ENDPOINT_URL)
        return DynamoDbClient {
            region = DYNAMODB_REGION
            endpointResolver = StaticEndpointResolver(endpoint)
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun MutableMap<String, AttributeValue>.addContent(run: NuclearRunDB) {
    run::class.declaredMemberProperties.forEach {
        when (it.returnType) {
            uuid -> this[it.name] = AttributeValue.S(run.id)
            string -> this[it.name] = AttributeValue.S(it.getter.call(run) as String)
            int -> this[it.name] = AttributeValue.N((it.getter.call(run) as Int).toString())
            boolean -> this[it.name] = AttributeValue.Bool(it.getter.call(run) as Boolean)
            list -> this[it.name] = AttributeValue.Ss(it.getter.call(run) as List<String>)
            char -> this[it.name] = AttributeValue.S((it.getter.call(run) as Char).toString())
            long -> this[it.name] = AttributeValue.N((it.getter.call(run) as Long).toString())
        }
        println("Added ${it.name} to the item. Value is ${it.getter.call(run)}")
    }
}