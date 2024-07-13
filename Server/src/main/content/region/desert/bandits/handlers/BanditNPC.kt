package content.region.desert.bandits.handlers

import core.api.God
import core.api.hasGodItem
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class BanditNPC(id: Int = NPCs.BANDIT_1926, location: Location? = null) : AbstractNPC(id, location) {
    private val supportRange: Int = 3

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return BanditNPC(id, location)
    }

    override fun tick() {
        if (!inCombat()) {
            val players = RegionManager.getLocalPlayers(this, 5)
            for (player in players) {
                if (player.inCombat()) continue
                if (hasGodItem(player, God.SARADOMIN)) {
                    sendChat("Time to die, Saradominist filth!")
                    attack(player)
                    break
                } else if (hasGodItem(player, God.ZAMORAK)) {
                    sendChat("Prepare to suffer, Zamorakian scum!")
                    attack(player)
                    break
                }
            }
        }
        super.tick()
    }

    //Clear target on death
    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
    }

    override fun onImpact(entity: Entity?, state: BattleState?) {
        if (entity is Player) {
                RegionManager.getLocalNpcs(entity, supportRange).forEach {
                    if (it.id == NPCs.BANDIT_1926 && !it.properties.combatPulse.isAttacking && it != this) {
                        it.sendChat("You picked the wrong place to start trouble!")
                        it.attack(entity)
                    }
                }
        }
        super.onImpact(entity, state)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BANDIT_1926)
    }

}