package content.global.ame.events.surpriseexam

import content.global.ame.RandomEventNPC
import content.global.ame.kidnapPlayer
import core.api.*
import org.rs09.consts.NPCs
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Sounds

class MysteriousOldManNPC(var type: String = "", override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.MYSTERIOUS_OLD_MAN_410) {
    override fun init() {
        super.init()
        sendChat("Surprise exam, ${player.username}!")
        queueScript(player, 4, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    lock(player, 6)
                    sendGraphics(Graphics(1576, 0, 0), player.location)
                    animate(player,8939)
                    playAudio(player, Sounds.TELEPORT_ALL_200)
                    return@queueScript delayScript(player, 3)
                }
                1 -> {
                    kidnapPlayer(player, Location(1886, 5025, 0), TeleportManager.TeleportType.INSTANT)
                    return@queueScript stopExecuting(player)
                }
                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    override fun talkTo(npc: NPC) {
        sendMessage(player, "He isn't interested in talking to you.")
    }
}
