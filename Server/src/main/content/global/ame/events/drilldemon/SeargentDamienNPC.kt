package content.global.ame.events.drilldemon

import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs
import content.global.ame.RandomEventNPC
import core.api.*
import core.api.utils.WeightBasedTable

import core.game.interaction.QueueStrength
import core.game.system.timer.impl.AntiMacro
import core.tools.secondsToTicks

class SeargentDamienNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.SERGEANT_DAMIEN_2790) {

    override fun init() {
        super.init()
        sendChat(player.username.capitalize() + "! Drop and give me 20!")
        queueScript(player, 4, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    lock(player, secondsToTicks(30))
                    DrillDemonUtils.teleport(player)
                    AntiMacro.terminateEventNpc(player)
                    return@queueScript delayScript(player, 2)
                }
                1 -> {
                    openDialogue(player, SeargentDamienDialogue(isCorrect = true, eventStart = true), NPCs.SERGEANT_DAMIEN_2790)
                    return@queueScript stopExecuting(player)
                }
                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    override fun talkTo(npc: NPC) {
        openDialogue(player, SeargentDamienDialogue(), npc)
    }
}