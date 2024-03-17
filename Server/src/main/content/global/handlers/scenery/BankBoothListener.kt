package content.global.handlers.scenery

import core.api.*
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import core.ServerConstants
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import content.global.handlers.npc.BankerNPC
import core.game.world.repository.Repository

/**
 * Allows the user to interact with bank booths.
 *
 * @author vddCore
 */
class BankBoothListener : InteractionListener {

    companion object {
        val INOPERABLE_BANK_BOOTHS = intArrayOf(
            Scenery.BANK_BOOTH_12800, Scenery.BANK_BOOTH_12801, Scenery.BANK_BOOTH_36262, Scenery.BANK_BOOTH_35648
        )

        val BANK_BOOTHS = intArrayOf(
            Scenery.BANK_BOOTH_2213, Scenery.BANK_BOOTH_2214, Scenery.BANK_BOOTH_3045, Scenery.BANK_BOOTH_5276,
            Scenery.BANK_BOOTH_6084, Scenery.BANK_BOOTH_10517, Scenery.BANK_BOOTH_11338, Scenery.BANK_BOOTH_11402,
            Scenery.BANK_BOOTH_11758, Scenery.BANK_BOOTH_12798, Scenery.BANK_BOOTH_12799, Scenery.BANK_BOOTH_14367,
            Scenery.BANK_BOOTH_14368, Scenery.BANK_BOOTH_16700, Scenery.BANK_BOOTH_18491, Scenery.BANK_BOOTH_19230,
            Scenery.BANK_BOOTH_20325, Scenery.BANK_BOOTH_20326, Scenery.BANK_BOOTH_20327, Scenery.BANK_BOOTH_20328,
            Scenery.BANK_BOOTH_22819, Scenery.BANK_BOOTH_24914, Scenery.BANK_BOOTH_25808, Scenery.BANK_BOOTH_26972,
            Scenery.BANK_BOOTH_29085, Scenery.BANK_BOOTH_30015, Scenery.BANK_BOOTH_30016, Scenery.BANK_BOOTH_34205,
            Scenery.BANK_BOOTH_34752, Scenery.BANK_BOOTH_35647, Scenery.BANK_BOOTH_36786, Scenery.BANK_BOOTH_37474
        )

        public fun convertToNotes (used: Node, player: Player) {
            val item = used as Item

            if (item.noteChange != item.id) {
                if (item.definition.isUnnoted) {
                    val amount = amountInInventory(player, item.id)
                    if (removeItem(player, Item(item.id, amount))) {
                        addItem(player, item.noteChange, amount)
                    }
                } else {
                    var amount = item.amount
                    val freeSlotCount = freeSlots(player)

                    // If there is exactly one more note than free slots
                    // the note disappearing can be used as the last slot
                    if (amount > freeSlotCount && amount != freeSlotCount + 1) {
                            amount = freeSlotCount
                        }

                    if (removeItem(player, Item(item.id, amount))) {
                        addItem(player, item.noteChange, amount)
                    }
                }

                return
            }

            sendMessage(player, "This item can't be noted.")
        }
    }

    /**
     * Searches an area in the order described
     * by each number with X being the location
     * of node being interacted with.
     *
     *    [1]
     * [4][X][2]
     *    [3]
     */
    private fun locateAdjacentBankerLinear(node: Node): NPC? {
        for (dir in arrayOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)) {
            Repository.findNPC(node.location.transform(dir))?.let { return it as? BankerNPC }
        }

        return null
    }

    /**
     * If size is 1 then the following method
     * searches a square area in the following
     * order with X being the location of node
     * that was interacted with.
     *
     * ->[   ]
     *   [ X ]
     *   [   ]->
     *
     */
    private fun locateAdjacentBankerSquare(node: Node, size: Int = 1): NPC? {
        for (y in (node.location.y - size)..(node.location.y + size)) {
            for (x in (node.location.x - size)..(node.location.x + size)) {
                Repository.findNPC(Location(x, y))?.let { return it as? BankerNPC }
            }
        }

        return null
    }

    private fun tryInvokeBankerDialogue(player: Player, node: Node) {
        /**
         * First, we look for regular bankers that neatly stand in front of the bank booth.
         * If that fails, we expand the search to a larger area.
         */
        (locateAdjacentBankerLinear(node) ?: locateAdjacentBankerSquare(node, 2))?.let {
            if (core.game.dialogue.DialogueInterpreter.contains(it.id)) {
                it.faceLocation(node.location)
                openDialogue(player, it.id, NPC(it.id, it.location))
            } else {
                openDialogue(player, NPCs.BANKER_494)
            }
        }
    }

    private fun quickBankBoothUse(player: Player, node: Node, state: Int): Boolean {
        if (player.ironmanManager.checkRestriction(IronmanMode.ULTIMATE)) {
            return true
        }

        if (BankerNPC.checkLunarIsleRestriction(player, node)) {
            tryInvokeBankerDialogue(player, node)
            return true
        }

        openBankAccount(player)
        return true
    }

    private fun regularBankBoothUse(player: Player, node: Node, state: Int): Boolean {
        if (player.ironmanManager.checkRestriction(IronmanMode.ULTIMATE)) {
            return true
        }

        if (ServerConstants.BANK_BOOTH_QUICK_OPEN) {
            return quickBankBoothUse(player, node, state)
        }

        tryInvokeBankerDialogue(player, node)
        return true
    }

    private fun collectBankBoothUse(player: Player, node: Node, state: Int): Boolean {
        if (BankerNPC.checkLunarIsleRestriction(player, node)) {
            tryInvokeBankerDialogue(player, node)
            return true
        }

        openGrandExchangeCollectionBox(player)
        return true
    }

    private fun attemptToConvertItems(player: Player, used: Node, with: Node): Boolean {
        if (!hasOption(with, "use")) {
            sendMessage(player, "You shouldn't be able to do that with object ${with.id}.")
            sendMessage(player, "Please screenshot this and report to the developers.")

            return true
        }

        if (!ServerConstants.BANK_BOOTH_NOTE_UIM && player.ironmanManager.checkRestriction(IronmanMode.ULTIMATE)) {
            return true
        }

        if (BankerNPC.checkLunarIsleRestriction(player, with)) {
            tryInvokeBankerDialogue(player, with)
            return true
        }
        
        convertToNotes (used, player)
        return true
    }

    override fun defineListeners() {
        defineInteraction(IntType.SCENERY, BANK_BOOTHS, "use-quickly", "bank", handler = ::quickBankBoothUse)
        defineInteraction(IntType.SCENERY, BANK_BOOTHS,  "use", handler = ::regularBankBoothUse)
        defineInteraction(IntType.SCENERY, BANK_BOOTHS, "collect", handler = ::collectBankBoothUse)

        if (ServerConstants.BANK_BOOTH_NOTE_ENABLED) {
            onUseAnyWith(IntType.SCENERY, *BANK_BOOTHS, handler = ::attemptToConvertItems)
        }
    }
}
