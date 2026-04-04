package content.global.skill.construction.decoration.pohstorage

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Animations
import org.rs09.consts.Items
import org.rs09.consts.Sounds
import org.rs09.consts.Scenery as Obj

/**
 * Handles interactions with storage objects.
 */
@Initializable
class StorageOptionPlugin : OptionHandler() {

    /**
     * Represents all storage boxes & configuration.
     */
    enum class StorageBox(val objectIds: IntArray, val storableFamily: StorableFamily? = null, val openable: Boolean = false, val closable: Boolean = false, val capacity: Int = Int.MAX_VALUE) {

        /* Bookcase
         * Wooden:   all
         * Oak:      all
         * Mahogany: all */
        BOOKCASE(intArrayOf(Obj.BOOKCASE_13597, Obj.BOOKCASE_13598, Obj.BOOKCASE_13599), StorableFamily.BOOKCASE),

        /* Cape Rack
         * Oak:      normal capes
         * Teak:     1 skillcape
         * Mahogany: 5 skillcapes
         * Gilded:   10 skillcapes
         * Marble:   all
         * Magic:    all */
        CAPE_RACK(intArrayOf(Obj.OAK_CAPE_RACK_18766), StorableFamily.CAPE_RACK),
        CAPE_RACK_ONE(intArrayOf(Obj.TEAK_CAPE_RACK_18767), StorableFamily.CAPE_RACK_SKILL, capacity = 1),
        CAPE_RACK_FIVE(intArrayOf(Obj.MAHOGANY_CAPE_RACK_18768), StorableFamily.CAPE_RACK_SKILL, capacity = 5),
        CAPE_RACK_TEN(intArrayOf(Obj.GILDED_CAPE_RACK_18769), StorableFamily.CAPE_RACK_SKILL, capacity = 10),
        CAPE_RACK_ALL(intArrayOf(Obj.MARBLE_CAPE_RACK_18770, Obj.MAGIC_CAPE_RACK_18771), StorableFamily.CAPE_RACK_SKILL),

        /* Fancy Dress Box
         * Oak:      2
         * Teak:     4
         * Mahogany: all */
        FANCY_BOX_2(intArrayOf(Obj.FANCY_DRESS_BOX_18773), StorableFamily.FANCY_DRESS, false, true, 2),
        FANCY_BOX_4(intArrayOf(Obj.FANCY_DRESS_BOX_18775), StorableFamily.FANCY_DRESS, false, true, 4),
        FANCY_BOX_ALL(intArrayOf(Obj.FANCY_DRESS_BOX_18777), StorableFamily.FANCY_DRESS, false, true),
        FANCY_BOX_OPEN(intArrayOf(Obj.FANCY_DRESS_BOX_18772, Obj.FANCY_DRESS_BOX_18774, Obj.FANCY_DRESS_BOX_18776), openable = true),

        /* Toy Box
         * Oak:      all
         * Teak:     all
         * Mahogany: all */
        TOY_BOX_OPEN(intArrayOf(Obj.TOY_BOX_18798, Obj.TOY_BOX_18800, Obj.TOY_BOX_18802), openable = true),
        TOY_BOX_CLOSE(intArrayOf(Obj.TOY_BOX_18799, Obj.TOY_BOX_18801, Obj.TOY_BOX_18803), closable = true, storableFamily = StorableFamily.TOY_BOX),

        /* Treasure Chest
         * Oak:      Low
         * Teak:     Low, Medium
         * Mahogany: Low, Medium, High */
        TREASURE_LOW(intArrayOf(Obj.TREASURE_CHEST_18805), StorableFamily.TREASURE_CHEST_LOW),
        TREASURE_MED(intArrayOf(Obj.TREASURE_CHEST_18807), StorableFamily.TREASURE_CHEST_MED),
        TREASURE_HIGH(intArrayOf(Obj.TREASURE_CHEST_18809), StorableFamily.TREASURE_CHEST_HIGH),
        TREASURE_OPEN(intArrayOf(Obj.TREASURE_CHEST_18804, Obj.TREASURE_CHEST_18806, Obj.TREASURE_CHEST_18808), openable = true),

        /* Magic Wardobe
         * Oak:         1 set
         * Carved oak:  2 sets
         * Teak:        3 sets
         * Carved Teak: 4 sets
         * Mahogany:    5 sets
         * Gilded:      6 sets
         * Marble:      all sets */
        WARDROBE_1(intArrayOf(Obj.MAGIC_WARDROBE_18785), StorableFamily.MAGIC_WARDROBE, false, true, 1),
        WARDROBE_2(intArrayOf(Obj.MAGIC_WARDROBE_18787), StorableFamily.MAGIC_WARDROBE, false, true, 2),
        WARDROBE_3(intArrayOf(Obj.MAGIC_WARDROBE_18789), StorableFamily.MAGIC_WARDROBE, false, true, 3),
        WARDROBE_4(intArrayOf(Obj.MAGIC_WARDROBE_18791), StorableFamily.MAGIC_WARDROBE, false, true, 4),
        WARDROBE_5(intArrayOf(Obj.MAGIC_WARDROBE_18793), StorableFamily.MAGIC_WARDROBE, false, true, 5),
        WARDROBE_6(intArrayOf(Obj.MAGIC_WARDROBE_18795), StorableFamily.MAGIC_WARDROBE, false, true, 6),
        WARDROBE_ALL(intArrayOf(Obj.MAGIC_WARDROBE_18797), StorableFamily.MAGIC_WARDROBE),
        WARDROBE_OPEN(intArrayOf(Obj.MAGIC_WARDROBE_18784, Obj.MAGIC_WARDROBE_18786, Obj.MAGIC_WARDROBE_18788, Obj.MAGIC_WARDROBE_18790, Obj.MAGIC_WARDROBE_18792, Obj.MAGIC_WARDROBE_18794, Obj.MAGIC_WARDROBE_18796), openable = true),

        /* Armour Case
         * Oak:      2
         * Teak:     4
         * Mahogany: all */
        ARMOUR_2(intArrayOf(Obj.ARMOUR_CASE_18779), StorableFamily.ARMOUR_CASE, false, true, 2),
        ARMOUR_4(intArrayOf(Obj.ARMOUR_CASE_18781), StorableFamily.ARMOUR_CASE, false, true, 4),
        ARMOUR_ALL(intArrayOf(Obj.ARMOUR_CASE_18783), StorableFamily.ARMOUR_CASE),
        ARMOUR_OPEN(intArrayOf(Obj.ARMOUR_CASE_18778, Obj.ARMOUR_CASE_18780, Obj.ARMOUR_CASE_18782), openable = true)
    }

    private val allBoxes = StorageBox.values()

    override fun newInstance(arg: Any?): Plugin<Any> {
        allBoxes.forEach { box ->
            box.objectIds.forEach { id ->
                SceneryDefinition.forId(id)?.let { def ->
                    if (box.storableFamily != null) def.handlers["option:search"] = this
                    if (box.openable) def.handlers["option:open"] = this
                    if (box.closable) def.handlers["option:close"] = this
                }
            }
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val obj = node as Scenery
        val box = allBoxes.firstOrNull { obj.id in it.objectIds } ?: return true

        when (option) {
            "search" -> {
                when (obj.id) {
                    // special handling for med and high level treasure chests since they have a dialogue option first
                    Obj.TREASURE_CHEST_18807 -> {
                        sendDialogueOptions(player, "What sort of Treasure Trail items do you want to see?", "Low-level rewards", "Medium-level rewards")
                        addDialogueAction(player) { p, button ->
                            val family = when (button) {
                                2 -> StorableFamily.TREASURE_CHEST_LOW
                                3 -> StorableFamily.TREASURE_CHEST_MED
                                else -> null
                            }
                            family?.let { StorageInterface.openStorage(p, it, box) }
                            return@addDialogueAction
                        }
                    }
                    Obj.TREASURE_CHEST_18809 -> {
                        sendDialogueOptions(player, "What sort of Treasure Trail items do you want to see?", "Low-level rewards", "Medium-level rewards", "High-level rewards")
                        addDialogueAction(player) { p, button ->
                            val family = when (button) {
                                2 -> StorableFamily.TREASURE_CHEST_LOW
                                3 -> StorableFamily.TREASURE_CHEST_MED
                                4 -> StorableFamily.TREASURE_CHEST_HIGH
                                else -> null
                            }
                            family?.let { StorageInterface.openStorage(p, it, box) }
                            return@addDialogueAction
                        }
                    }
                    // bookcases need to re-store the games book in them on open. I think the bookshelf is the only source.
                    Obj.BOOKCASE_13597 -> {
                        storeBookInHouse(player, Items.GAME_BOOK_7681)
                        box.storableFamily?.let { StorageInterface.openStorage(player, it, box) }
                    }
                    Obj.BOOKCASE_13598 -> {
                        storeBookInHouse(player, Items.GAME_BOOK_7681)
                        box.storableFamily?.let { StorageInterface.openStorage(player, it, box) }
                    }
                    Obj.BOOKCASE_13599 -> {
                        storeBookInHouse(player, Items.GAME_BOOK_7681)
                        box.storableFamily?.let { StorageInterface.openStorage(player, it, box) }
                    }
                    else -> {
                        // default open for rest
                        box.storableFamily?.let { StorageInterface.openStorage(player, it, box) }
                    }
                }
            }
            "open" -> if (box.openable) openBox(player, obj)
            "close" -> if (box.closable) closeBox(player, obj)
        }
        return true
    }

    private fun openBox(player: Player, obj: Scenery) {
        playAudio(player, Sounds.CHEST_OPEN_52)
        animate(player, Animations.HUMAN_OPEN_CHEST_536)
        replaceScenery(obj, obj.id + 1, -1)
    }

    private fun closeBox(player: Player, obj: Scenery) {
        playAudio(player, Sounds.CHEST_CLOSE_51)
        animate(player, 538)
        replaceScenery(obj, obj.id - 1, -1)
    }
}
