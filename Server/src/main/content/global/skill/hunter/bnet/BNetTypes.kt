package content.global.skill.hunter.bnet

import core.api.inInventory
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Graphics as GFX
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Enumerates the creatures that a player can catch with a butterfly net.
 * @author Bishop
 */

enum class BNetTypes(@JvmField val node: BNetNode) {

    /**
     * BUTTERFLIES
     */

    RUBY_HARVEST(
        BNetNode(
            intArrayOf(NPCs.RUBY_HARVEST_5085),
            intArrayOf(15, 80, 75), doubleArrayOf(24.0, 300.0, 50.0),
            arrayOf<Graphics>(Graphics(GFX.RUBY_HARVEST_TRAIL_913), Graphics(GFX.RUBY_HARVEST_SIMPLE_917)),
            Item(Items.RUBY_HARVEST_10020))
    ),
    SAPPHIRE_GLACIALIS(
        BNetNode(
            intArrayOf(NPCs.SAPPHIRE_GLACIALIS_5084, NPCs.SAPPHIRE_GLACIALIS_7499),
            intArrayOf(25, 85, 80), doubleArrayOf(34.0, 400.0, 70.0),
            arrayOf<Graphics>(Graphics(GFX.SAPPHIRE_GLACIALIS_TRAIL_912), Graphics(GFX.SAPPHIRE_GLACIALIS_SIMPLE_916)),
            Item(Items.SAPPHIRE_GLACIALIS_10018))
    ),
    SNOWY_KNIGHT(
        BNetNode(
            intArrayOf(NPCs.SNOWY_KNIGHT_5083, NPCs.SNOWY_KNIGHT_7498),
            intArrayOf(35, 90, 85), doubleArrayOf(44.0, 500.0, 100.0),
            arrayOf<Graphics>(Graphics(GFX.SNOWY_KNIGHT_TRAIL_911), Graphics(GFX.SNOWY_KNIGHT_SIMPLE_915)),
            Item(Items.SNOWY_KNIGHT_10016))
    ),
    BLACK_WARLOCK(
        BNetNode(
            intArrayOf(NPCs.BLACK_WARLOCK_5082),
            intArrayOf(45, 95, 90), doubleArrayOf(54.0, 650.0, 125.0),
            arrayOf<Graphics>(Graphics(GFX.BLACK_WARLOCK_TRAIL_910), Graphics(GFX.BLACK_WARLOCK_SIMPLE_914)),
            Item(Items.BLACK_WARLOCK_10014))
    ),

    /**
     * IMPLINGS
     */

    BABY_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.BABY_IMPLING_1028, NPCs.BABY_IMPLING_6055),
            17, 20.0, 18.0,
            Item(Items.BABY_IMPLING_JAR_11238)
        )
    ),
    YOUNG_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.YOUNG_IMPLING_1029, NPCs.YOUNG_IMPLING_6056),
            22, 22.0, 20.0,
            Item(Items.YOUNG_IMPLING_JAR_11240)
        )
    ),
    GOURMET_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.GOURMET_IMPLING_1030, NPCs.GOURMET_IMPLING_6057),
            28, 24.0, 22.0,
            Item(Items.GOURM_IMPLING_JAR_11242)
        )
    ),
    EARTH_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.EARTH_IMPLING_1031, NPCs.EARTH_IMPLING_6058),
            36, 27.0, 25.0,
            Item(Items.EARTH_IMPLING_JAR_11244)
        )
    ),
    ESSENCE_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.ESSENCE_IMPLING_1032, NPCs.ESSENCE_IMPLING_6059),
            42, 29.0, 27.0,
            Item(Items.ESS_IMPLING_JAR_11246)
        )
    ),
    ECLECTIC_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.ECLECTIC_IMPLING_1033, NPCs.ECLECTIC_IMPLING_6060),
            50, 32.0, 30.0,
            Item(Items.ECLECTIC_IMPLING_JAR_11248)
        )
    ),
    NATURE_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.NATURE_IMPLING_1034, NPCs.NATURE_IMPLING_6061),
            58, 36.0, 34.0,
            Item(Items.NATURE_IMPLING_JAR_11250)
        )
    ),
    MAGPIE_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.MAGPIE_IMPLING_1035, NPCs.MAGPIE_IMPLING_6062),
            65, 216.0, 44.0,
            Item(Items.MAGPIE_IMPLING_JAR_11252), 500
        )
    ),
    NINJA_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.NINJA_IMPLING_6053, NPCs.NINJA_IMPLING_6063),
            74, 240.0, 50.0,
            Item(Items.NINJA_IMPLING_JAR_11254), 2000
        )
    ),
    /**
     * INSERT PIRATE IMPLING HERE
     */
    DRAGON_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.DRAGON_IMPLING_6054, NPCs.DRAGON_IMPLING_6064),
            83, 300.0, 65.0,
            Item(Items.DRAGON_IMPLING_JAR_11256), 3000
        )
    );

    companion object {
        @JvmField
        val implings: Array<ImplingNode> = BNetTypes.values().filter { it.node is ImplingNode }.map { it.node as ImplingNode }.toTypedArray()

        fun getImpling(player: Player): ImplingNode? {
            for (imp in implings) {
                if (inInventory(player, imp.reward.id)) {
                    return imp
                }
            }
            return null
        }

        @JvmStatic
        fun getBNetByNPC(npc: NPC): BNetTypes? {
            for (type in values()) {
                for (id in type.node.npcs) {
                    if (id == npc.id) {
                        return type
                    }
                }
            }
            return null
        }

        @JvmStatic
        fun getBNetByItem(item: Item): BNetNode? {
            for (type in values()) {
                if (type.node.reward.id == item.id) {
                    return type.node
                }
            }
            return null
        }
    }
}
