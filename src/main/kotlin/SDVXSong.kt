package team.shavibot.mirai.plugin

import kotlinx.serialization.Serializable
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage

@Serializable
data class SDVXSong(
    val name: String,
    val composer: String,
    val bpm: String,
    val effector: String,
    val s_difficulty: String,
    val description: String,
    val cover: String
) {
    override fun toString(): String {
        return "Song: $name\nComposer: $composer\nBPM: $bpm\nEffector: $effector\nS-Difficulty: $s_difficulty\nDescription: $description"
    }
    suspend fun toMessage(contact: Contact): Message {
        return PluginMain.getResourceAsStream("image\\$cover")!!.uploadAsImage(contact)+toString()
    }
}
