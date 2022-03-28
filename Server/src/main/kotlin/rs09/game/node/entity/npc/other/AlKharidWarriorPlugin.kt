package rs09.game.node.entity.npc.other

import core.cache.def.impl.NPCDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.plugin.Initializable
import core.plugin.Plugin
import rs09.plugin.ClassScanner

/**
 * Handles the Al-Kharid Warrior
 * @author Ceikry
 */
@Initializable
class AlKharidWarriorNPC : AbstractNPC {
    var target: Player? = null

    //Constructor spaghetti because Arios I guess
    constructor() : super(18, null, true) {}
    private constructor(id: Int, location: Location) : super(id, location) {}
    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return AlKharidWarriorNPC(id, location)
    }

    override fun newInstance(arg: Any?): Plugin<Any?> {
        ClassScanner.definePlugin(WarriorOptionPlugin())
        return super.newInstance(arg)
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
        super.finalizeDeath(killer)
        target = null
    }

    override fun getIds(): IntArray {
        return ID
    }

    inner class WarriorOptionPlugin : OptionHandler() {
        override fun newInstance(arg: Any?): Plugin<Any?> {
            for (id in ID) {
                NPCDefinition.forId(id).handlers["option:attack"] = this
            }
            return this
        }

        override fun handle(player: Player, node: Node, option: String): Boolean {
            RegionManager.getLocalNpcs(player,6).forEach{
                if(it.id == 18 && it != node.asNpc() && !it.properties.combatPulse.isAttacking){
                    it.sendChat("Brother, I shall help thee with this infidel!")
                    (it as AlKharidWarriorNPC).target = player
                    it.attack(player)
                }
            }
            player.attack(node.asNpc())
            return true
        }

        override fun isWalk(): Boolean {
            return false
        }
    }

    companion object {
        /**
         * The NPC ids of NPCs using this plugin.
         */
        private val ID = intArrayOf(18)
    }
}