package content.global.ame.events.freakyforester

import content.global.ame.RandomEventManager
import content.global.ame.RandomEventNPC
import core.api.*
import org.rs09.consts.NPCs
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Sounds

class FreakyForesterNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.FREAKY_FORESTER_2458) {

    override fun init() {
        super.init()
        lock(player, 8)
        submitWorldPulse(object : Pulse() {
            var counter = 0
            override fun pulse(): Boolean {
                when (counter++) {
                    0 -> sendChat("Ah, ${player.username}, just the person I need!")
                    4 -> {
                        sendGraphics(Graphics(308, 100, 50), player.location)
                        animate(player,714)
                        playAudio(player, Sounds.TELEPORT_ALL_200)
                    }
                    7 -> {
                        FreakUtils.teleport(player)
                        FreakUtils.giveFreakTask(player)
                        RandomEventManager.getInstance(player)!!.event?.terminate()
                        openDialogue(player, FreakyForesterDialogue(), FreakUtils.freakNpc)
                        resetAnimator(player)
                    }
                }
                return false
            }
        })
    }

    override fun talkTo(npc: NPC) {
        player.dialogueInterpreter.open(FreakyForesterDialogue(),npc)
    }
}