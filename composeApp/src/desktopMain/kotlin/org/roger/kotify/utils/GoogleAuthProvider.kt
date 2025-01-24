package org.roger.kotify.utils

import com.google.auth.oauth2.GoogleCredentials
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import java.io.File

interface GoogleAuthProvider {
    suspend fun getAccessTokenFromLocalPath(serviceAccountJson: String): String
    suspend fun getAccessTokenFromUrl(serviceAccountJsonUrl: String): String
}

class FcmClient(
    private val client: HttpClient,
    private val authProvider: GoogleAuthProvider
) {
    suspend fun sendNotification(
        projectId: String,
        messageBody: FcmMessage,
        serviceAccountJson: String
    ): HttpResponse {
        println("Message: $messageBody")
        val accessToken = authProvider.getAccessTokenFromLocalPath(serviceAccountJson)

        val url = "https://fcm.googleapis.com/v1/projects/$projectId/messages:send"

        val response: HttpResponse = client.post(url) {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            setBody(messageBody)
        }

        println("Response: ${response.status}")
        println("Response: ${response.bodyAsText()}")

        return response
    }
}

class GoogleAuthProviderImpl(
    private val client: HttpClient
) : GoogleAuthProvider {
    override suspend fun getAccessTokenFromLocalPath(serviceAccountJson: String): String {
        val credentials = GoogleCredentials
            .fromStream(File(serviceAccountJson).inputStream())
            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
        credentials.refreshIfExpired()

        return credentials.accessToken.tokenValue
    }

    override suspend fun getAccessTokenFromUrl(serviceAccountJsonUrl: String): String {
        val serviceAccountJsonContent = client.get(serviceAccountJsonUrl).bodyAsText()

        val credentials = GoogleCredentials
            .fromStream(serviceAccountJsonContent.byteInputStream())
            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
        credentials.refreshIfExpired()

        return credentials.accessToken.tokenValue
    }
}

@Serializable
data class FcmMessage(
    val message: FcmNotification
)

@Serializable
data class FcmNotification(
    val token: String,
    val data: JsonElement
)