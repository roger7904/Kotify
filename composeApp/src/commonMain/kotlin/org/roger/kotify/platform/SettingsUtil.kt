package org.roger.kotify.platform

import kotlinx.serialization.Serializable

expect object SettingsUtil {
    fun saveSettings(settings: UserSettings)
    fun loadSettings(): UserSettings
}

@Serializable
data class UserSettings(
    val serviceAccountPath: String = "",
    val projectId: String = "",
    val targetDeviceToken: String = "",
    val externalJson: String = ""
)