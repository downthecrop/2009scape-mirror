package rs09.game.content.quest.members.elementalworkshop

import api.getTool
import api.sendDialogue
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.gather.SkillingTool
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

/**
 * Npc(s):
 *  Earth elemental - ID: 4910 (Attack-able form)
 *  Elemental rock  - ID: 4911 (Sleeping "mine-able" form)
 * Option(s):
 *  "Mine"
 *
 *  @author Woah, with love
 */
@Initializable
class ElementalRockNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id,location), InteractionListener {

    private val ELEMENTAL_ROCK_TRANSFORMATION_4865 = Animation(4865)

    // Sanity transform back to elemental rock
    override fun init() {
        isWalks = false
        isNeverWalks = true
        transform(NPCs.ELEMENTAL_ROCK_4911)
        super.init()
    }

    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return ElementalRockNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.EARTH_ELEMENTAL_4910, NPCs.ELEMENTAL_ROCK_4911)
    }

    // The NPC stays in their upright form when the player
    // stops attacking and can walk around
    override fun defineListeners() {
        on(ids, IntType.NPC, "Mine") { player, node ->
            val tool: SkillingTool? = getTool(player, true)
            // Check to see if the player can proc the NPC
            if (tool == null) {
                sendDialogue(player, "You do not have a pickaxe which you have the Mining level to use.")
                return@on true
            }
            // Warn player they don't have a high enough mining level
            if (player.skills.getStaticLevel(Skills.MINING) < 20) {
                sendDialogue(player, "You need a mining level of at least 20 to mine elemental ore.")
                return@on true
            }
            // Transform the current NPC to 4910 (monster form)
            pulseManager.run(object : Pulse() {
                var count = 0
                override fun pulse(): Boolean {
                    when (count) {
                        // Play the transformation Animation
                        0 -> node.asNpc().animate(ELEMENTAL_ROCK_TRANSFORMATION_4865)
                        // Yell at the player and transform into the combat NPC 4910
                        1 -> {
                            node.asNpc().sendChat("Grr... Ge'roff us!")
                            node.asNpc().transform(NPCs.EARTH_ELEMENTAL_4910)
                        }
                        // Start attacking the player
                        3 -> {
                            node.asNpc().attack(player)
                            node.asNpc().isNeverWalks = false
                            node.asNpc().isWalks = true
                            return true
                        }
                    }
                    count++
                    return false
                }
            })
            return@on true
        }
    }

    // Reset back to rock NPC
    override fun finalizeDeath(killer: Entity?) {
        isWalks = false
        isNeverWalks = true
        transform(NPCs.ELEMENTAL_ROCK_4911)
        super.finalizeDeath(killer)
    }
}