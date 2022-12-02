package com.angelparraga.services.db

import com.angelparraga.NuclearRunDB
import io.andrewohara.dynamokt.DataClassTableSchema
import software.amazon.awssdk.regions.Region

const val NUCLEAR_RUN_TABLE = "NuclearRuns"
//const val DYNAMODB_REGION_NAME = "eu-west-1"
val DYNAMODB_REGION: Region = Region.EU_WEST_1
const val AWS_ENDPOINT_URL = "http://localhost:8000"

val tableSchema = DataClassTableSchema(NuclearRunDB::class)
