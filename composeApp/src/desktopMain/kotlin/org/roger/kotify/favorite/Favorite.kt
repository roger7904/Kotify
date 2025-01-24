package org.roger.kotify.favorite

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File
import java.util.UUID

object FavoritesStorage {
    private val userHome = System.getProperty("user.home")
    private val appSupportDir = File("$userHome/Library/Application Support/Kotify")
    private val favoritesFile = File(appSupportDir, "favorites.json")
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    init {
        if (!appSupportDir.exists()) {
            appSupportDir.mkdirs()
        }
    }

    fun saveFavorites(favorites: List<FavoriteItem>) {
        val jsonString = json.encodeToString(ListSerializer(FavoriteItem.serializer()), favorites)
        favoritesFile.writeText(jsonString)
    }

    fun loadFavorites(): List<FavoriteItem> {
        return if (favoritesFile.exists()) {
            val jsonString = favoritesFile.readText()
            json.decodeFromString(jsonString)
        } else {
            emptyList()
        }
    }
}

@Serializable
data class FavoriteItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val serviceAccountPath: String,
    val projectId: String,
    val targetDeviceToken: String,
    val jsonMessage: String = "",
)