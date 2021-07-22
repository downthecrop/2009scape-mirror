package rs09.game.node.entity.npc.other

import api.ContentAPI
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class BlastFurnaceOre : AbstractNPC {
    //Arios constructor spaghetti
    constructor() : super(NPCs.COAL_2562, null, true) {}
    private constructor(id: Int, location: Location) : super(id, location) {}

    constructor(owner: Player, variant: BFOreVariant) : super(
        when(variant){
        BFOreVariant.COPPER       -> NPCs.COPPER_ORE_2555
        BFOreVariant.TIN          -> NPCs.TIN_ORE_2554
        BFOreVariant.COAL         -> NPCs.COAL_2562
        BFOreVariant.MITHRIL      -> NPCs.MITHRIL_ORE_2557
        BFOreVariant.ADAMANT      -> NPCs.ADAMANTITE_ORE_2558
        BFOreVariant.SILVER       -> NPCs.SILVER_ORE_2560
        BFOreVariant.GOLD         -> NPCs.GOLD_ORE_2561
        BFOreVariant.PERFECT_GOLD -> NPCs.PERFECT_GOLD_ORE_2563
        BFOreVariant.RUNITE       -> NPCs.RUNITE_ORE_2559
    }, Location.create(1942, 4966, 0)) {this.owner = owner; isRespawn = false}

    var owner: Player? = null
    var delay = 2
    var counter = 2

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return BlastFurnaceOre(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
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
        delay--
        if(delay <= 0 && ContentAPI.getWorldTicks() % 2 == 0) { //run every other tick
            if(counter > 0){
                properties.teleportLocation = location.transform(0, -1, 0)
                counter--
            } else {
                //increment ore count, update interface, whatever you need to do here.
                clear()
            }
        }
    }

}

enum class BFOreVariant {
    COPPER,
    TIN,
    COAL,
    MITHRIL,
    ADAMANT,
    SILVER,
    GOLD,
    PERFECT_GOLD,
    RUNITE
}