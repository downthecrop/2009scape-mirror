package core.game.worldevents.holiday.halloween.randoms

import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.worldevents.holiday.HolidayRandomEventNPC
import core.game.worldevents.holiday.HolidayRandoms
import core.tools.RandomFunction
import org.rs09.consts.Sounds

class DeathHolidayRandomNPC() : HolidayRandomEventNPC(2862) {
    override fun init() {
        super.init()
        playJingle(player, 337)
        queueScript(this, 6, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    this.face(player)
                    visualize(this, 864, -1)
                    playGlobalAudio(this.location, Sounds.ZOMBIE_MOAN_2324)
                    return@queueScript delayScript(this, 1)
                }
                1 -> {
                    when(RandomFunction.getRandom(2)) {
                        0 -> sendChat(this, "Your end is near, ${player.username.capitalize()}...")
                        1 -> sendChat(this, "Time is running out, ${player.username.capitalize()}...")
                        2 -> sendChat(this, "Tick tock, ${player.username.capitalize()}...")
                    }
                    return@queueScript delayScript(this, 4)
                }
                2 -> {
                    HolidayRandoms.terminateEventNpc(player)
                    return@queueScript stopExecuting(this)
                }
                else -> return@queueScript stopExecuting(this)
            }
        }
    }

    override fun talkTo(npc: NPC) {
    }
}