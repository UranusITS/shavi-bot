package team.shavibot.mirai.plugin

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

var difficultyWord = mapOf(0 to "exh",1 to "mxm",2 to "inf",3 to "grv",4 to "hvn",5 to "vvd")

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
        else songs.songs[(songs.songs.indices).random()]
    }
    fun getRandom(target : String): SDVXSong? {
        return if (songs.songs.isEmpty()) null
        else {
            var targetLevel = ""
            var targetDifficulty = ""
            if(target[0] in '0'..'9'){
                var pos=0
                while(target[pos] in '0'..'9'){
                    targetLevel += target[pos]
                    pos++
                    if(pos >= target.length) break
                }
                targetDifficulty = target.replaceFirst(targetLevel,"").toLowerCase()
            }else{
                var pos=0
                while(target[pos] !in '0'..'9'){
                    targetDifficulty += target[pos]
                    pos++
                    if(pos >= target.length) break
                }
                targetDifficulty = targetDifficulty.toLowerCase()
                if(pos<target.length){
                    while(target[pos] in '0'..'9'){
                    targetLevel += target[pos]
                    pos++
                    if(pos >= target.length) break
                    }
                }
            }
            var tmpList = mutableListOf<SDVXSong>()
            for(song in songs.songs)
                if((targetLevel == "" || song.level.toString() == targetLevel))
                    if(targetDifficulty == "" || difficultyWord[song.difficultyKind] == targetDifficulty) tmpList.add(song)
            return if(tmpList.isEmpty()) null
            else tmpList[(tmpList.indices).random()]
        }
    }
}