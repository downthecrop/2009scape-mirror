package core.game.worldevents.holiday.halloween.randoms

import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.NPC
import core.game.world.update.flag.context.Animation
import core.game.worldevents.holiday.HolidayRandomEventNPC
import core.game.worldevents.holiday.HolidayRandoms
import org.rs09.consts.Sounds

class SpiderHolidayRandomNPC() : HolidayRandomEventNPC(61) {
    override fun init() {
        super.init()
        this.behavior = SpiderHolidayRandomBehavior()
        playGlobalAudio(this.location, Sounds.SPIDER_4375)
        var stomped = false
        queueScript(this,4, QueueStrength.SOFT) { stage: Int ->
        when (stage) {
            0 -> {
                sendChat(player, "Eww a spider!")
                return@queueScript delayScript(this, 2)
            }
            1 -> {
                if (player.location.withinDistance(this.location, 3)) {
                    player.animate(Animation(4278))
                    playGlobalAudio(this.location, Sounds.UNARMED_KICK_2565)
                    sendMessage(player, "You stomp the spider.")
                    stomped = true
                }
                return@queueScript delayScript(this, 1)
            }
            2 -> {
                if (stomped) {
                    impact(this, 1, ImpactHandler.HitsplatType.NORMAL)
                } else {
                    sendMessage(player, "The spider runs away.")
                    playGlobalAudio(this.location, Sounds.SPIDER_4375)
                    HolidayRandoms.terminateEventNpc(player)
                }
                return@queueScript stopExecuting(this)
            }
            else -> return@queueScript stopExecuting(this)
        }
        }
    }

    override fun talkTo(npc: NPC) {
    }
}