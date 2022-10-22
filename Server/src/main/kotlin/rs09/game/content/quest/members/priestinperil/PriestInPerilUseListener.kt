package rs09.game.content.quest.members.priestinperil

import api.*
import core.game.content.dialogue.FacialExpression
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListener

/**
 * Listener for Priest in Peril usage interactions
 * @author Byte
 */
class PriestInPerilUseListener : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.TINDERBOX_590, Scenery.MONUMENT_3493) { player, used, _ ->
            if (!getAttribute(player, "priest_in_peril:tinderbox", false) && removeItem(player, used)) {
                addItem(player, Items.GOLDEN_TINDERBOX_2946)
                sendMessage(player, "You swap the tinderbox for the golden tinderbox.")
                setAttribute(player, "/save:priest_in_peril:tinderbox", true)
            }

            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.CANDLE_36, Scenery.MONUMENT_3494) { player, used, _ ->
            if (!getAttribute(player, "priest_in_peril:candle", false) && removeItem(player, used)) {
                addItem(player, Items.GOLDEN_CANDLE_2947)
                sendMessage(player, "You swap the candle for the golden candle.")
                setAttribute(player, "/save:priest_in_peril:candle", true)
            }

            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.EMPTY_POT_1931, Scenery.MONUMENT_3495) { player, used, _ ->
            if (!getAttribute(player, "priest_in_peril:pot", false) && removeItem(player, used)) {
                addItem(player, Items.GOLDEN_POT_2948)
                sendMessage(player, "You swap the pot for the golden pot.")
                setAttribute(player, "/save:priest_in_peril:pot", true)
            }

            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.HAMMER_2347, Scenery.MONUMENT_3496) { player, used, _ ->
            if (!getAttribute(player, "priest_in_peril:hammer", false) && removeItem(player, used)) {
                addItem(player, Items.GOLDEN_HAMMER_2949)
                sendMessage(player, "You swap the hammer for the golden hammer.")
                setAttribute(player, "/save:priest_in_peril:hammer", true)
            }

            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.FEATHER_314, Scenery.MONUMENT_3497) { player, used, _ ->
            if (!getAttribute(player, "priest_in_peril:feather", false) && removeItem(player, Item(used.id, 1))) {
                addItem(player, Items.GOLDEN_FEATHER_2950)
                sendMessage(player, "You swap the feather for the golden feather.")
                setAttribute(player, "/save:priest_in_peril:feather", true)
            }

            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.NEEDLE_1733, Scenery.MONUMENT_3498) { player, used, _ ->
            if (!getAttribute(player, "priest_in_peril:needle", false) && removeItem(player, Item(used.id, 1))) {
                addItem(player, Items.GOLDEN_NEEDLE_2951)
                sendMessage(player, "You swap the needle for the golden needle.")
                setAttribute(player, "/save:priest_in_peril:needle", true)
            }

            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.GOLDEN_KEY_2944, Scenery.MONUMENT_3499) { player, used, _ ->
            if (!getAttribute(player, "priest_in_peril:key", false) && removeItem(player, used)) {
                addItem(player, Items.IRON_KEY_2945)
                sendMessage(player, "You swap the golden key for the iron key.")
                setAttribute(player, "/save:priest_in_peril:key", true)
            }

            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.BUCKET_1925, Scenery.WELL_3485) { player, used, _ ->
            if (!removeItem(player, used)) {
                return@onUseWith false
            }

            addItem(player, Items.BUCKET_OF_WATER_2953)
            sendMessage(player, "You fill the bucket from the well.")

            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.IRON_KEY_2945, Scenery.CELL_DOOR_3463) { player, used, _ ->
            if (!removeItem(player, used)) {
                return@onUseWith false
            }

            setQuestStage(player, "Priest in Peril", 15)
            sendMessage(player, "You have unlocked the cell door.")

            val npc = core.game.node.entity.npc.NPC.create(NPCs.DREZEL_7690, player.location)
            npc.name = "Drezel"
            sendNPCDialogue(player, npc.id, "Oh! Thank you! You have found the key!", FacialExpression.HALF_GUILTY)

            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.BUCKET_OF_WATER_2954, Scenery.MORYTANIA_COFFIN_30728) { player, used, _ ->
            if (!removeItem(player, used)) {
                return@onUseWith false
            }

            addItem(player, Items.BUCKET_1925)
            setQuestStage(player, "Priest in Peril", 16)
            sendMessage(player, "You pour the blessed water over the coffin...")

            return@onUseWith true
        }
    }
}
