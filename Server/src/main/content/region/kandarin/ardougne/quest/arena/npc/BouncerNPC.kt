package content.region.kandarin.ardougne.quest.arena.npc

import content.region.kandarin.ardougne.quest.arena.FightArena
import content.region.kandarin.ardougne.quest.arena.dialogue.GeneralKhazardDialogue
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class BouncerNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    var clearTime = 0
    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return BouncerNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BOUNCER_269)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (clearTime++ > 300) poofClear(this)
    }

    companion object {
        fun spawnBouncer(player: Player) {
            val bouncer = BouncerNPC(NPCs.BOUNCER_269)
            bouncer.location = location(2604, 3160, 0)
            bouncer.isWalks = true
            bouncer.isAggressive = true
            bouncer.isActive = false

            if (bouncer.asNpc() != null && bouncer.isActive && getAttribute(player, "spawn-bouncer", false)) {
                bouncer.properties.teleportLocation = bouncer.properties.spawnLocation
            }
            bouncer.isActive = true
            GameWorld.Pulser.submit(object : Pulse(2, bouncer) {
                override fun pulse(): Boolean {
                    bouncer.init()
                    bouncer.attack(player)
                    return true
                }
            })
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            val quest = "Fight Arena"
            if (getQuestStage(killer, quest) >= 89) {
                setQuestStage(killer, FightArena.FightArenaQuest, 91)
            }
            removeAttribute(killer, "spawn-bouncer")
            openDialogue(killer, GeneralKhazardDialogue())
        }
        clear()
        super.finalizeDeath(killer)
    }
}