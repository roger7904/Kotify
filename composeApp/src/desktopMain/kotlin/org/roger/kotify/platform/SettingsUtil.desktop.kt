package org.roger.kotify.platform

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

actual object SettingsUtil {
    private val userHome = System.getProperty("user.home")
    private val appSupportDir = File("$userHome/Library/Application Support/Kotify")
    private val settingsFile = File(appSupportDir, "user_settings_v1.0.json")
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    init {
        if (!appSupportDir.exists()) {
            appSupportDir.mkdirs()
        }
    }

    actual fun saveSettings(settings: UserSettings) {
        val jsonString = json.encodeToString(settings)
        settingsFile.writeText(jsonString)
    }

    actual fun loadSettings(): UserSettings {
        return try {
            val jsonString = settingsFile.readText()
            json.decodeFromString(jsonString)
        } catch (_: Exception) {
            UserSettings()
        }
    }
}