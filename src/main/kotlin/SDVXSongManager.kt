package team.shavibot.mirai.plugin

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class SDVXSongManager {
    @Serializable
    data class SDVXSongs(val songs: List<SDVXSong>)
    private var songs = SDVXSongs(emptyList<SDVXSong>())
    fun init() {
        val jsonFile = PluginMain.getResource("data/SDVXSongs.json")
        if (jsonFile.isNullOrEmpty())
            PluginMain.logger.error("data/SDVXSongs.json not found")
        else songs = Json.decodeFromString(jsonFile.toString())
    }
    fun getRandom(): SDVXSong? {
        return if (songs.songs.isEmpty()) null
        else songs.songs[(0 until songs.songs.size).random()]
    }
}