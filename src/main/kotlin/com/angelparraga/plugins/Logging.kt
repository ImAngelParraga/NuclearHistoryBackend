package com.angelparraga.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import org.slf4j.event.Level

fun Application.configureLogging() {
    install(CallLogging) {
        level = Level.INFO
        format { call ->
            runBlocking {
                val requestBody = call.receiveText()
                val httpMethod = call.request.httpMethod.value
                val endpoint = call.request.uri
                val headers = call.request.headers.toMap().entries.joinToString()
                val status = call.response.status()

                """
                    Request: 
                        Method: $httpMethod
                        Endpoint: $endpoint
                        Headers: $headers
                        Body: $requestBody
                       
                    Response Status: $status
                """.trimIndent()
            }
        }
    }
}