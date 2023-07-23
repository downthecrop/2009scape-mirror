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
class ScorpionNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    var clearTime = 0
    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return ScorpionNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KHAZARD_SCORPION_271)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (clearTime++ > 300) poofClear(this)
    }

    companion object {
        fun spawnScorpion(player: Player) {
            val scorpion = ScorpionNPC(NPCs.KHAZARD_SCORPION_271)
            scorpion.location = location(2604, 3159, 0)
            scorpion.isWalks = true
            scorpion.isAggressive = true
            scorpion.isActive = false

            if (scorpion.asNpc() != null && scorpion.isActive && getAttribute(player, "spawn-scorpion", false)) {
                scorpion.properties.teleportLocation = scorpion.properties.spawnLocation
            }
            scorpion.isActive = true
            GameWorld.Pulser.submit(object : Pulse(2, scorpion) {
                override fun pulse(): Boolean {
                    scorpion.init()
                    scorpion.attack(player)
                    return true
                }
            })
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            val quest = "Fight Arena"
            if (getQuestStage(killer, quest) == 88) {
                setQuestStage(killer, FightArena.FightArenaQuest, 89)
            }
            removeAttribute(killer, "spawn-scorpion")
            openDialogue(killer, GeneralKhazardDialogue())
        }
        clear()
        super.finalizeDeath(killer)
    }
}