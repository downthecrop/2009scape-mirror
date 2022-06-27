//package rs09.game.node.entity.npc.city.piscatoris
//
//import core.game.node.entity.npc.AbstractNPC
//import core.game.world.map.Location
//import core.plugin.Initializable
//import org.rs09.consts.NPCs
//import rs09.game.interaction.InteractionListener
//
//@Initializable
//class ArnoldLydsporNPC : AbstractNPC, InteractionListener {
//    //Constructor spaghetti because Arios I guess
//    constructor() : super(0, null)
//    private constructor(id: Int, location: Location) : super(id, location)
//
//    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
//        return ArnoldLydsporNPC(id, location)
//    }
//
//    override fun getIds(): IntArray = intArrayOf(NPCs.ARNOLD_LYDSPOR_3824)
//}