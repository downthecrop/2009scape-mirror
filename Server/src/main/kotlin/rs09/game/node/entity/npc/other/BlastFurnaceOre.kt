package rs09.game.node.entity.npc.other

import api.*
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.content.activity.blastfurnace.BlastFurnace

/**On god this is like 95% ceikry and 5% me adding a setAttribute to the clear code
 * @author funny alium man*/

@Initializable
class BlastFurnaceOre : AbstractNPC {
    //Arios constructor spaghetti
    constructor() : super(NPCs.COAL_2562, null, true) {}
    private constructor(id: Int, location: Location) : super(id, location) {}

    constructor(owner: Player, variant: BFOreVariant, amount: Int) : super(
        when(variant){
            BFOreVariant.IRON         -> NPCs.IRON_ORE_2556
            BFOreVariant.COPPER       -> NPCs.COPPER_ORE_2555
            BFOreVariant.TIN          -> NPCs.TIN_ORE_2554
            BFOreVariant.COAL         -> NPCs.COAL_2562
            BFOreVariant.MITHRIL      -> NPCs.MITHRIL_ORE_2557
            BFOreVariant.ADAMANT      -> NPCs.ADAMANTITE_ORE_2558
            BFOreVariant.SILVER       -> NPCs.SILVER_ORE_2560
            BFOreVariant.GOLD         -> NPCs.GOLD_ORE_2561
            BFOreVariant.RUNITE       -> NPCs.RUNITE_ORE_2559
        }, Location.create(1942, 4966, 0)) {this.owner = owner; isRespawn = false; }

    var owner: Player? = null
    var delay = 2
    var counter = 3


    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return BlastFurnaceOre(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.IRON_ORE_2556,
            NPCs.COPPER_ORE_2555,
            NPCs.TIN_ORE_2554,
            NPCs.COAL_2562,
            NPCs.MITHRIL_ORE_2557,
            NPCs.ADAMANTITE_ORE_2558,
            NPCs.SILVER_ORE_2560,
            NPCs.GOLD_ORE_2561,
            NPCs.PERFECT_GOLD_ORE_2563,
            NPCs.RUNITE_ORE_2559)
    }

    override fun handleTickActions() {
        if (BlastFurnace.beltRunning) {
            delay--
            if (delay <= 0 && getWorldTicks() % 2 == 0) { //run every other tick
                if (counter > 0) {
                    properties.teleportLocation = location.transform(0, -1, 0)
                    counter--
                    if (counter == 0) {
                        val animation = Animation(2434)
                        animate(animation)
                        owner?.setAttribute("/save:OreInPot",true)
                        submitIndividualPulse(this, object : Pulse(animationDuration(animation)) {
                            override fun pulse(): Boolean {
                                clear()
                                return true
                            }
                        })
                    }
                }
            }
        }
    }
}

enum class BFOreVariant {
    IRON,
    COPPER,
    TIN,
    COAL,
    MITHRIL,
    ADAMANT,
    SILVER,
    GOLD,
    RUNITE
}