package content.global.ame.events.pillory

import content.global.ame.RandomEventNPC
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

// "::revent [-p] <lt>player name<gt> [-e <lt>event name<gt>]"
class PilloryNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.PILLORY_GUARD_2791) {

    override fun init() {
        super.init()
        sendChat("${player.username}, you're under arrest!")
        face(player)
        player.dialogueInterpreter.sendPlainMessage(true, "", "Solve the pillory puzzle to be returned to where you came from.")
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
                    if (getAttribute<Location?>(player, PilloryInterface.PILLORY_ATRRIBUTE_RETURN_LOC, null) == null) {
                        setAttribute(player, PilloryInterface.PILLORY_ATRRIBUTE_RETURN_LOC, player.location)
                    }
                    PilloryInterface.initPillory(player)
                    teleport(player, PilloryInterface.LOCATIONS.random()) // 9 random spots!
                    AntiMacro.terminateEventNpc(player)
                    sendGraphics(Graphics(1577, 0, 0), player.location)
                    animate(player,8941)
                    return@queueScript stopExecuting(player)
                }
                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    override fun talkTo(npc: NPC) {
        //player.dialogueInterpreter.open(FreakyForesterDialogue(),npc)
    }
}