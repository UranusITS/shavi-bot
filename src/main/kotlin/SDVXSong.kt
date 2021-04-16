package team.shavibot.mirai.plugin

import kotlinx.serialization.Serializable
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage

val sdvxDifficultyKind = mapOf(0 to "EXHAUST",1 to "MAXIMUM", 2 to "INFINITE", 3 to "GRAVITY", 4 to "HEAVENLY", 5 to "VIVID")

@Serializable
data class SDVXSong(
    val name: String,
    val level: Int,
    val difficultyKind: Int,
    val composer: String,
    val bpm: String,
    val effector: String,
    val s_difficulty: String,
    val description: String,
    val cover: String
) {
    override fun toString(): String {
        return "$name\n"+sdvxDifficultyKind[difficultyKind]+""" / $level
$composer
BPM: $bpm
Effector: $effector
S-Difficulty: $s_difficulty
Description: $description"""
    }
    suspend fun toMessage(contact: Contact): Message {
        return PluginMain.getResourceAsStream("image/$cover")!!.uploadAsImage(contact)+toString()
    }
}
