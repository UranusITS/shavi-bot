package team.shavibot.mirai.plugin

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageRecallEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.Dice
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageSource
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.utils.info
import net.mamoe.mirai.utils.ExternalResource.Companion.sendAsImageTo
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource

//主体
object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "team.shavibot.mirai-plugin",
        name = "SHAVI-BOT",
        version = "0.1.0"
    ) {
        author("Mercury")

        info("""
            这是SHAVI-BOT, 
            很SHAVI.
        """.trimIndent())

        // author 和 info 可以删除.
    }
) {
    //管理者QQ
    private const val adminQQ : Long = 397083976

    //白名单群聊
    private val whiteGroup = setOf(894195753 ,934764491 ,692264063)

    //test回复
    private val testReply = arrayOf("呵呵，我还没死","这群也就这了，成天研究个机器人在不在")

    //复读
    private var lastMessage : MutableMap <Long ,Pair<MessageChain,Boolean>> = mutableMapOf()

    //防撤回
    private var twoMinutesMessage : MutableSet <MessageChain> = mutableSetOf()

    //加载图片
    private val SDVXSongManage = SDVXSongManager()

    override fun onEnable() {
        logger.info { "Plugin loaded" }

        SDVXSongManage.init()

        //管理员消息
        globalEventChannel().subscribeAlways<FriendMessageEvent> {
            //检查是否管理员
            if (sender.id != adminQQ){
                return@subscribeAlways
            }

            //查看状态
            if (message.contentToString() == "/test"){
                sender.sendMessage("呵呵，我还没死")
                return@subscribeAlways
            }

            //帮助
            if (message.contentToString() == "/help"){
                val tmpMessage = "管理员功能\n1./test 查看状态\n2./help 查看帮助\n\n群聊功能\n1./test 查看状态\n2./dice 丢骰子" +
                    "\n3.复读"
                sender.sendMessage(tmpMessage)
                return@subscribeAlways
            }
        }

        //群消息
        globalEventChannel().subscribeAlways<GroupMessageEvent> {
            //检查是否白名单
            if (whiteGroup.all { it.toLong() != group.id }) {
                return@subscribeAlways
            }

            //保存&删除消息
            twoMinutesMessage.add(message)
            for(messages in twoMinutesMessage) {
                if(time - messages[MessageSource]!!.time >= 120) {
                    twoMinutesMessage.remove(messages)
                }
            }

            //测试是否可用
            if (message.contentToString() == "/test"){
                val randoms = (0..1).random()
                group.sendMessage(testReply[randoms])
                return@subscribeAlways
            }

            //骰子
            if (message.contentToString() == "/dice"){
                val randoms = (1..6).random()
                //val randomDice = Dice(6)
                val randomDice = Dice(randoms)
                group.sendMessage(randomDice)
                return@subscribeAlways
            }

            //随个18
            if (message.contentToString() == "/random18"){
                group.sendMessage(SDVXSongManage.getRandom().toMessage(group))
                return@subscribeAlways
            }

            //复读
            if (lastMessage[group.id] != null) {
                if (lastMessage[group.id]!!.first.contentEquals(message, ignoreCase = false, strict = true)) {
                    if(lastMessage[group.id]!!.second) {
                        group.sendMessage(message)
                        lastMessage[group.id] = Pair(lastMessage[group.id]!!.first, false)
                        return@subscribeAlways
                    }
                } else {
                    lastMessage[group.id] = Pair(message, true)
                }
            } else {
                lastMessage[group.id] = Pair(message, true)
            }

        }

        //群撤回消息
        globalEventChannel().subscribeAlways<MessageRecallEvent.GroupRecall>{
            //白名单判断
            if (whiteGroup.all { it.toLong() != group.id }) {
                return@subscribeAlways
            }
            val allQuoteMessage = mutableSetOf<MessageChain>()
            for(message in twoMinutesMessage){
                if(message[MessageSource]!!.ids.contentEquals(messageIds))
                    allQuoteMessage.add(message)
            }
            if(allQuoteMessage.isNotEmpty()) {
                group.sendMessage(allQuoteMessage.single().quote() + "心虚了是吧撤回？")
            }else {
                group.sendMessage("我看你也只会撤回了")
            }
        }


        /*
        globalEventChannel().subscribeAlways<FriendMessageEvent>{
            //好友信息
            sender.sendMessage("hi")
        }
        globalEventChannel().subscribeAlways<NewFriendRequestEvent>{
            //自动同意好友申请
            accept()
        }
        globalEventChannel().subscribeAlways<BotInvitedJoinGroupRequestEvent>{
            //自动同意加群申请
            accept()
        }*/
    }
}