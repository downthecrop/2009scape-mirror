package rs09.game.interaction.item.withitem

import api.*
import core.game.node.entity.player.Player
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import rs09.game.interaction.InteractionListener

class OilStillListeners : InteractionListener() {
    override fun defineListeners() {
        val flowers = (Items.FLOWERS_2460..Items.FLOWERS_2477).toIntArray()
        val stills = intArrayOf(*getChildren(5908), 5909)
        val fillableItems = mapOf(
            Items.OIL_LAMP_4525 to Items.OIL_LAMP_4522,
            Items.OIL_LANTERN_4535 to Items.OIL_LANTERN_4537,
            Items.BULLSEYE_LANTERN_4546 to Items.BULLSEYE_LANTERN_4548,
            Items.SAPPHIRE_LANTERN_4700 to Items.SAPPHIRE_LANTERN_4701
        )

        onUseWith(ITEM, flowers, Items.ANCHOVY_OIL_11264) { player, used, with ->
            if (removeItem(player, used, Container.INVENTORY)) {
                replaceSlot(player, with.asItem().slot, Items.IMP_REPELLENT_11262.asItem())
                sendMessage(player, "You mix the flower petals with the anchovy oil to make a very strange-smelling concoction.")
            }
            return@onUseWith true
        }

        onUseWith(SCENERY, Items.SWAMP_TAR_1939, *stills) {player, used, _ ->
            if(checkStillEmpty(player)){
                removeItem(player, used)
                setVarbit(player, 425, 4, 2, save = true)
                sendMessage(player, "You refine some swamp tar into lamp oil.")
            } else {
                sendStillFull(player)
            }
            return@onUseWith true
        }

        onUseWith(SCENERY, fillableItems.keys.toIntArray(), *stills){player, used, _ ->
            when {
                checkStillEmpty(player) -> sendMessage(player, "There is no oil in the sill.")
                getStillState(player) == 2 -> {
                    val replacement = fillableItems[used.id]?.asItem() ?: return@onUseWith false
                    replaceSlot(player, used.asItem().slot, replacement)
                    setVarbit(player, 425, 4, 0)
                    sendMessage(player, "You fill the ${replacement.name.toLowerCase()} with oil.")
                }
                else -> sendMessage(player, "There is refined imp repellent in this still, not lamp oil.")
            }
            return@onUseWith true
        }

        onUseWith(SCENERY, Items.IMP_REPELLENT_11262, *stills){player, used, _ ->
            when {
                checkStillEmpty(player) -> {
                    replaceSlot(player, used.asItem().slot, Items.VIAL_229.asItem())
                    sendMessage(player, "You refine some imp repellent.")
                    setVarbit(player, 425, 4, 4)
                }
                else -> sendStillFull(player)
            }
            return@onUseWith true
        }

        onUseWith(SCENERY, Items.BUTTERFLY_JAR_10012, *stills){player, used, _ ->
            when {
                checkStillEmpty(player) -> sendMessage(player, "There is no refined imp repellent in the still.")
                getStillState(player) == 2 -> {
                    replaceSlot(player, used.asItem().slot, Items.IMPLING_JAR_11260.asItem())
                    setVarbit(player, 425, 4, 0)
                    sendMessage(player, "You turn the butterfly jar into an impling jar.")
                }
                else -> sendMessage(player, "There is lamp oil in this still, not refined imp repellent.")
            }
            return@onUseWith true
        }
    }

    private fun checkStillEmpty(player: Player) : Boolean {
        val stillState = getStillState(player)
        return stillState == 0
    }

    private fun sendStillFull(player: Player) {
        val stillState = getStillState(player)

        if(stillState != 0) {
            when(stillState) {
                2 -> sendMessage(player, "There is already lamp oil in the still.")
                4 -> sendMessage(player, "There is already imp repellent in the still.")
            }
        }
    }

    private fun getStillState(player: Player): Int {
        return getVarbitValue(player, 425, 4)
    }
}