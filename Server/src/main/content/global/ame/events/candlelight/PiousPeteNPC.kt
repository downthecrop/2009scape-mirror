package content.global.ame.events.candlelight

import content.global.ame.RandomEventNPC
import content.global.ame.kidnapPlayer
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

/** "::revent -p player_name -e candlelight" **/
class PiousPeteNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.PRIEST_3206) {

    override fun init() {
        super.init()
        // Supposed to be "I'm sorry to drag you away from your tasks, but I need a little help with something." but it's too goddamn long.
        sendChat("${player.username.capitalize()}! I need a little help with something.")
        face(player)
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
                    CandlelightInterface.initCandlelight(player)
                    kidnapPlayer(player, Location(1972, 5002, 0), TeleportManager.TeleportType.INSTANT)
                    // AntiMacro.terminateEventNpc(player)
                    sendGraphics(Graphics(1577, 0, 0), player.location)
                    animate(player,8941)
                    openDialogue(player, PiousPeteStartingDialogueFile(), NPC(NPCs.PIOUS_PETE_3207))
                    return@queueScript stopExecuting(player)
                }
                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    override fun talkTo(npc: NPC) {
        player.dialogueInterpreter.open(PiousPeteDialogueFile(),npc)
    }
}