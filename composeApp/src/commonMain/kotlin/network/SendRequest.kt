package network

import SERVER_PORT
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class SendRequest {
    private val client = HttpClient()
    private val serverUrl = "http://localhost:$SERVER_PORT/"

    public suspend fun callRoot(): String {
        val response = client.get(serverUrl)
        return response.bodyAsText()
    }
}
