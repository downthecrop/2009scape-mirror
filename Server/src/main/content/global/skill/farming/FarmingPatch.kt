package content.global.skill.farming

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.node.scenery.Scenery
import core.game.node.entity.player.Player
import content.global.skill.farming.timers.CropGrowth

enum class FarmingPatch(val varbit: Int, val type: PatchType) {
    //Allotments
    S_FALADOR_ALLOTMENT_NW(708,PatchType.ALLOTMENT),
    S_FALADOR_ALLOTMENT_SE(709,PatchType.ALLOTMENT),
    CATHERBY_ALLOTMENT_N(710,PatchType.ALLOTMENT),
    CATHERBY_ALLOTMENT_S(711,PatchType.ALLOTMENT),
    ARDOUGNE_ALLOTMENT_S(713,PatchType.ALLOTMENT),
    ARDOUGNE_ALLOTMENT_N(712,PatchType.ALLOTMENT),
    PORT_PHAS_ALLOTMENT_NW(714,PatchType.ALLOTMENT),
    PORT_PHAS_ALLOTMENT_SE(715,PatchType.ALLOTMENT),
    HARMONY_ISLAND_ALLOTMENT(3402,PatchType.ALLOTMENT),

    //Herb
    CATHERBY_HERB_CE(781,PatchType.HERB_PATCH),
    S_FALADOR_HERB_NE(780,PatchType.HERB_PATCH),
    ARDOUGNE_HERB_CE(782,PatchType.HERB_PATCH),
    PORT_PHAS_HERB_NE(783,PatchType.HERB_PATCH),
    TROLL_STRONGHOLD_HERB(2788,PatchType.HERB_PATCH),

    //Flower
    S_FALADOR_FLOWER_C(728,PatchType.FLOWER_PATCH),
    CATHERBY_FLOWER_C(729,PatchType.FLOWER_PATCH),
    ARDOUGNE_FLOWER_C(730,PatchType.FLOWER_PATCH),
    PORT_PHAS_FLOWER_C(731,PatchType.FLOWER_PATCH),
    WILDERNESS_FLOWER(5067,PatchType.FLOWER_PATCH),

    //Tree
    N_FALADOR_TREE(701,PatchType.TREE_PATCH),
    TAVERLY_TREE(700,PatchType.TREE_PATCH),
    GNOME_STRONGHOLD_TREE(2953,PatchType.TREE_PATCH),
    LUMBRIDGE_TREE(703,PatchType.TREE_PATCH),
    VARROCK_TREE(702,PatchType.TREE_PATCH),

    //Fruit Tree
    GNOME_STRONGHOLD_FRUIT_TREE(704,PatchType.FRUIT_TREE_PATCH),
    CATHERBY_FRUIT_TREE(707,PatchType.FRUIT_TREE_PATCH),
    TREE_GNOME_VILLAGE_FRUIT_TREE(705,PatchType.FRUIT_TREE_PATCH),
    BRIMHAVEN_FRUIT_TREE(706,PatchType.FRUIT_TREE_PATCH),
    LLETYA_FRUIT_TREE(4317,PatchType.FRUIT_TREE_PATCH),

    //Hops
    ENTRANA_HOPS(717,PatchType.HOPS_PATCH),
    LUMBRIDGE_HOPS(718,PatchType.HOPS_PATCH),
    MCGRUBOR_HOPS(719,PatchType.HOPS_PATCH),
    YANILLE_HOPS(716,PatchType.HOPS_PATCH),

    //Bushes
    CHAMPIONS_GUILD_BUSH(732,PatchType.BUSH_PATCH),
    RIMMINGTON_BUSH(733,PatchType.BUSH_PATCH),
    ARDOUGNE_BUSH(735,PatchType.BUSH_PATCH),
    ETCETERIA_BUSH(734,PatchType.BUSH_PATCH),

    //Spirit Tree
    ETCETERIA_SPIRIT_TREE(722,PatchType.SPIRIT_TREE_PATCH),
    PORT_SARIM_SPIRIT_TREE(720,PatchType.SPIRIT_TREE_PATCH),
    KARAMJA_SPIRIT_TREE(724,PatchType.SPIRIT_TREE_PATCH),

    //Other
    DRAYNOR_BELLADONNA(748, PatchType.BELLADONNA_PATCH),
    CANIFIS_MUSHROOM(746, PatchType.MUSHROOM_PATCH),
    ALKHARID_CACTUS(744, PatchType.CACTUS_PATCH),
    EVIL_TURNIP(4291, PatchType.EVIL_TURNIP_PATCH);


    companion object {
        @JvmField
        val patches = FarmingPatch.values().map { it.varbit to it }.toMap()
        val patchNodes = ArrayList<Int>()
        val nodeMap = HashMap<Int, SceneryDefinition>()

        init {
            patchNodes.addAll(8550..8557) //allotment wrappers
            patchNodes.addAll(7847..7853) //flower patch wrappers
            patchNodes.addAll(8150..8156) //herb patch wrappers
            patchNodes.addAll(8388..8391) // Tree patches
            patchNodes.add(19147) //Tree patch
            patchNodes.addAll(7962..7965) //fruit trees
            patchNodes.addAll(8173..8176) //hops
            patchNodes.addAll(7577..7580) //bush
            patchNodes.add(23760) //evil turnip
            patchNodes.add(7572) //belladonna
            patchNodes.add(8337) //mushroom
            patchNodes.add(27197) //jade vine
            patchNodes.add(7771) //cactus
            patchNodes.add(7807) //calquat
            patchNodes.addAll(8382..8383)//spirit trees
            patchNodes.add(8338) //spirit tree
            patchNodes.add(18816) //death plateau wrapper

            for (patch in patchNodes) {
                val def = SceneryDefinition.forId(patch)
                nodeMap[def.varbitID] = def
            }
        }

        @JvmStatic
        fun forObject(obj: Scenery): FarmingPatch?{
            return forObjectID(obj.id)
        }

        @JvmStatic
        fun forObjectID(id: Int): FarmingPatch?{
            val objDef = SceneryDefinition.forId(id)
            return patches[objDef.varbitID]
        }

        fun getSceneryDefByVarbit (id: Int) : SceneryDefinition? {
            return nodeMap[id]
        }
    }

    fun getPatchFor(player: Player, addPatch : Boolean = true): Patch{
        val crops = getOrStartTimer <CropGrowth> (player)
        return crops.getPatch(this, addPatch)
    }
}
