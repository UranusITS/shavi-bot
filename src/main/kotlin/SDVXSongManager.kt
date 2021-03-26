package team.shavibot.mirai.plugin

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class SDVXSongManager {
    @Serializable
    data class SDVXSongs(val songs: List<SDVXSong>)
    private var songs = SDVXSongs(emptyList<SDVXSong>())
    fun init() {
        val dataFile = PluginMain.getResource("data\\SDVXSongs.json").toString()
        songs = Json.decodeFromString(dataFile)
    }
    fun getRandom(): SDVXSong {
        return songs.songs[(0 until songs.songs.size).random()]
    }
}