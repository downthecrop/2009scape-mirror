package content.region.desert.alkharid.handlers

import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * Handles the Al-Kharid Warrior
 * @author Ceikry
 */
@Initializable
class AlKharidWarriorNPC : AbstractNPC {
    var target: Player? = null
    private val supportRange: Int = 5

    //Constructor spaghetti because Arios I guess
    constructor() : super(18, null, true) {}
    private constructor(id: Int, location: Location) : super(id, location) {}
    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return AlKharidWarriorNPC(id, location)
    }

    //Maintains target combat (resolves issue where they would just give up/walk away)
    override fun tick() {
        if(target != null && !inCombat() && skills.lifepoints > 0){
            attack(target)
        }
        super.tick()
    }

    //Clear target on death
    override fun finalizeDeath(killer: Entity?) {
        target = null
        super.finalizeDeath(killer)
    }

    override fun attack(node: Node?) {
        if (node is Player) target = node
        super.attack(node)
    }

    override fun onImpact(entity: Entity?, state: BattleState?) {
        if (entity is Player) {
            if (target == null) {
                target = entity
                RegionManager.getLocalNpcs(entity, supportRange).forEach {
                    if (it.id == NPCs.AL_KHARID_WARRIOR_18 && !it.properties.combatPulse.isAttacking && it != this) {
                        it.sendChat("Brother, I shall help thee with this infidel!")
                        it.attack(entity)
                    }
                }
            }
        }
        super.onImpact(entity, state)
    }

    override fun getIds(): IntArray {
        return ID
    }

    companion object {
        /**
         * The NPC ids of NPCs using this plugin.
         */
        private val ID = intArrayOf(NPCs.AL_KHARID_WARRIOR_18)
    }
}