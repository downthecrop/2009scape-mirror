package content.global.ame.events.freakyforester

import content.global.ame.RandomEventNPC
import core.api.*
import org.rs09.consts.NPCs
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.system.timer.impl.AntiMacro
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Sounds

class FreakyForesterNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.FREAKY_FORESTER_2458) {

    override fun init() {
        super.init()
        sendChat("Ah, ${player.username}, just the person I need!")
        queueScript(player, 4, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    lock(player, 6)
                    sendGraphics(Graphics(308, 100, 50), player.location)
                    animate(player,714)
                    playAudio(player, Sounds.TELEPORT_ALL_200)
                    return@queueScript delayScript(player, 3)
                }
                1 -> {
                    FreakUtils.teleport(player)
                    FreakUtils.giveFreakTask(player)
                    AntiMacro.terminateEventNpc(player)
                    openDialogue(player, FreakyForesterDialogue(), FreakUtils.freakNpc)
                    resetAnimator(player)
                    return@queueScript stopExecuting(player)
                }
                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    override fun talkTo(npc: NPC) {
        player.dialogueInterpreter.open(FreakyForesterDialogue(),npc)
    }
}