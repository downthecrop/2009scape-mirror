package rs09.game.node.entity.skill.farming

import core.cache.def.impl.ObjectDefinition
import core.cache.def.impl.VarbitDefinition
import core.game.node.`object`.GameObject
import core.game.node.entity.player.Player
import rs09.game.node.entity.state.newsys.states.FarmingState

enum class FarmingPatch(val varpIndex: Int, val varpOffset: Int, val type: PatchType) {
    //Allotments
    S_FALADOR_ALLOTMENT_NW(504,0,PatchType.ALLOTMENT),
    S_FALADOR_ALLOTMENT_SE(504,8,PatchType.ALLOTMENT),
    CATHERBY_ALLOTMENT_N(504,16,PatchType.ALLOTMENT),
    CATHERBY_ALLOTMENT_S(504,24,PatchType.ALLOTMENT),
    ARDOUGNE_ALLOTMENT_S(505,8,PatchType.ALLOTMENT),
    ARDOUGNE_ALLOTMENT_N(505,0,PatchType.ALLOTMENT),
    PORT_PHAS_ALLOTMENT_NW(505,16,PatchType.ALLOTMENT),
    PORT_PHAS_ALLOTMENT_SE(505,24,PatchType.ALLOTMENT),
    HARMONY_ISLAND_ALLOTMENT(982,8,PatchType.ALLOTMENT),

    //Herb
    CATHERBY_HERB_CE(515,8,PatchType.HERB),
    S_FALADOR_HERB_NE(515,0,PatchType.HERB),
    ARDOUGNE_HERB_CE(515,16,PatchType.HERB),
    PORT_PHAS_HERB_NE(515,24,PatchType.HERB),
    TROLL_STRONGHOLD_HERB(830,0,PatchType.HERB),

    //Flower
    S_FALADOR_FLOWER_C(508,0,PatchType.FLOWER),
    CATHERBY_FLOWER_C(508,8,PatchType.FLOWER),
    ARDOUGNE_FLOWER_C(508,16,PatchType.FLOWER),
    PORT_PHAS_FLOWER_C(508,24,PatchType.FLOWER),
    WILDERNESS_FLOWER(1183,12,PatchType.FLOWER),

    //Tree
    N_FALADOR_TREE(502,8,PatchType.TREE),
    TAVERLY_TREE(502,0,PatchType.TREE),
    GNOME_STRONGHOLD_TREE(830,11,PatchType.TREE),
    LUMBRIDGE_TREE(502,24,PatchType.TREE),
    VARROCK_TREE(502,16,PatchType.TREE),

    //Fruit Tree
    GNOME_STRONGHOLD_FRUIT_TREE(503,0,PatchType.FRUIT_TREE),
    CATHERBY_FRUIT_TREE(503,24,PatchType.FRUIT_TREE),
    TREE_GNOME_VILLAGE_FRUIT_TREE(503,8,PatchType.FRUIT_TREE),
    BRIMHAVEN_FRUIT_TREE(503,16,PatchType.FRUIT_TREE),
    LLETYA_FRUIT_TREE(830,23,PatchType.FRUIT_TREE),

    //Hops
    ENTRANA_HOPS(506,8,PatchType.HOPS),
    LUMBRIDGE_HOPS(506,16,PatchType.HOPS),
    MCGRUBOR_HOPS(506,24,PatchType.HOPS),
    YANILLE_HOPS(506,0,PatchType.HOPS),

    //Bushes
    CHAMPIONS_GUILD_BUSH(509,0,PatchType.BUSH),
    RIMMINGTON_BUSH(509,8,PatchType.BUSH),
    ARDOUGNE_BUSH(509,24,PatchType.BUSH),
    ETCETERIA_BUSH(509,16,PatchType.BUSH),

    //Spirit Tree
    ETCETERIA_SPIRIT_TREE(507,8,PatchType.SPIRIT_TREE),
    PORT_SARIM_SPIRIT_TREE(507,0,PatchType.SPIRIT_TREE),
    KARAMJA_SPIRIT_TREE(507,16,PatchType.SPIRIT_TREE);

    companion object {
        @JvmField
        val patches = FarmingPatch.values().map { (it.varpIndex shl it.varpOffset) to it }.toMap()

        @JvmStatic
        fun forObject(obj: GameObject): FarmingPatch?{
            return forObjectID(obj.id)
        }

        @JvmStatic
        fun forObjectID(id: Int): FarmingPatch?{
            val objDef = ObjectDefinition.forId(id)
            val def = VarbitDefinition.forObjectID(objDef.varbitID)
            return patches[def.configId shl def.bitShift]
        }
    }

    fun getPatchFor(player: Player): Patch{
        var state: FarmingState? = player.states.get("farming") as FarmingState?
        return if(state == null){
            state = player.registerState("farming") as FarmingState
            state.getPatch(this).also { state.init() }
        } else
            state.getPatch(this)
    }
}