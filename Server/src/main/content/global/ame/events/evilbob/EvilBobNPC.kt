package content.global.ame.events.evilbob

import content.global.ame.RandomEventNPC
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.system.timer.impl.AntiMacro
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

class EvilBobNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.EVIL_BOB_2478) {

    override fun init() {
        super.init()
        sendChat("meow")
        queueScript(player, 4, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    lock(player, 6)
                    sendChat(player, "No... what? Nooooooooooooo!")
                    animate(player, EvilBobUtils.teleAnim)
                    player.graphics(EvilBobUtils.telegfx)
                    playAudio(player, Sounds.TELEPORT_ALL_200)
                    EvilBobUtils.giveEventFishingSpot(player)
                    return@queueScript delayScript(player, 3)
                }
                1 -> {
                    sendMessage(player, "Welcome to Scape2009.")
                    EvilBobUtils.teleport(player)
                    resetAnimator(player)
                    openDialogue(player, EvilBobDialogue(), NPCs.EVIL_BOB_2479)
                    AntiMacro.terminateEventNpc(player)
                    return@queueScript stopExecuting(player)
                }
                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    override fun talkTo(npc: NPC) {
        openDialogue(player, EvilBobDialogue(), this.asNpc())
    }
}