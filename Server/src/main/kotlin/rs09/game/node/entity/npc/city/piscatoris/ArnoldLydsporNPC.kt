package rs09.game.node.entity.npc.city.piscatoris

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.game.node.entity.npc.other.banks.BankerNPC

@Initializable
class ArnoldLydsporNPC : AbstractNPC, InteractionListener {
    companion object {
        val NPC_IDS = intArrayOf(NPCs.ARNOLD_LYDSPOR_3824)
    }

    //Constructor spaghetti because Arios I guess
    constructor() : super(0, null)
    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
        return ArnoldLydsporNPC(id, location)
    }

    override fun getIds(): IntArray = NPC_IDS

    override fun defineListeners() {
        on(NPC_IDS, NPC, "bank", handler = BankerNPC::attemptBank)
        on(NPC_IDS, NPC, "collect", handler = BankerNPC::attemptCollect)
        /* Talk-to handled by NPCTalkListener */
    }
}