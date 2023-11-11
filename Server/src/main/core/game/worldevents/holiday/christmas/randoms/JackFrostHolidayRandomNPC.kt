package core.game.worldevents.holiday.christmas.randoms


import core.api.getPathableRandomLocalCoordinate
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.world.map.path.Pathfinder
import core.game.world.update.flag.context.Graphics
import core.game.worldevents.holiday.HolidayRandomEventNPC
import core.tools.minutesToTicks
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

class JackFrostHolidayRandomNPC : HolidayRandomEventNPC(NPCs.JACK_FROST_8517) {

    override fun init() {
        ticksLeft = minutesToTicks(1)
        spawnLocation = getPathableRandomLocalCoordinate(this, 3, player.location)
        super.init()
    }

    override fun tick() {
        if (getWorldTicks() % 5 == 0) {
            if (this.location.withinDistance(player.location, 1) || this.location.getDistance(player.location).toInt() > 3) {
                val path = Pathfinder.find(this, getPathableRandomLocalCoordinate(this, 3, player.location))
                path.walk(this)
            } else {
                throwSnowball()
            }
        }
        super.tick()
    }

    override fun talkTo(npc: NPC) {
        sendMessage(player, "He does not seem interested in talking.")
    }

    override fun follow() {
    }

    private fun throwSnowball() {
        queueScript(this, 0, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    face(this, player)
                    visualize(this, 7530, -1)
                    return@queueScript delayScript(this, 1)
                }
                1 -> {
                    Projectile.create(this, player, 861, 30, 10).send()
                    return@queueScript delayScript(this, this.location.getDistance(player.location).toInt())
                }
                2 -> {
                    player.graphics(Graphics(1282))
                    playGlobalAudio(player.location, Sounds.GUBLINCH_SNOWBALL_3295)
                    return@queueScript stopExecuting(this)
                }
                else -> return@queueScript stopExecuting(this)
            }
        }
    }
}