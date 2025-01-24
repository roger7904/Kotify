package org.roger.kotify.record

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File
import java.util.UUID

object SendRecordStorage {
    private val userHome = System.getProperty("user.home")
    private val appSupportDir = File("$userHome/Library/Application Support/Kotify")
    private val recordsFile = File(appSupportDir, "send_records_v1.0.json")
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    init {
        if (!appSupportDir.exists()) {
            appSupportDir.mkdirs()
        }
    }

    fun saveRecords(records: List<SendRecord>) {
        val jsonString = json.encodeToString(ListSerializer(SendRecord.serializer()), records)
        recordsFile.writeText(jsonString)
    }

    fun loadRecords(): List<SendRecord> {
        return if (recordsFile.exists()) {
            val jsonString = recordsFile.readText()
            json.decodeFromString(ListSerializer(SendRecord.serializer()), jsonString)
        } else {
            emptyList()
        }
    }
}

@Serializable
data class SendRecord(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val statusCode: Int,
    val statusDescription: String,
    val isSuccess: Boolean,
    val serviceAccountPath: String,
    val projectId: String,
    val targetDeviceToken: String,
    val externalJson: String = ""
)
