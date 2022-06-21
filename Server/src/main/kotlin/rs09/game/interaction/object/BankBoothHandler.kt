package rs09.game.interaction.`object`

import api.hasSealOfPassage
import api.openDialogue
import api.sendMessage
import core.game.content.dialogue.DialogueInterpreter
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import rs09.ServerConstants
import rs09.game.ge.GrandExchangeRecords
import rs09.game.interaction.InteractionListener
import rs09.game.node.entity.npc.other.BankerNPC
import rs09.game.world.repository.Repository

/**
 * Allows the user to interact with bank booths.
 *
 * @author vddCore
 */
class BankBoothHandler : InteractionListener {

    companion object {
        val BANK_BOOTHS = intArrayOf(
            Scenery.BANK_BOOTH_2213, Scenery.BANK_BOOTH_2214, Scenery.BANK_BOOTH_3045, Scenery.BANK_BOOTH_5276,
            Scenery.BANK_BOOTH_6084, Scenery.BANK_BOOTH_10517, Scenery.BANK_BOOTH_11338, Scenery.BANK_BOOTH_11402,
            Scenery.BANK_BOOTH_11758, Scenery.BANK_BOOTH_12798, Scenery.BANK_BOOTH_12799, Scenery.BANK_BOOTH_12800,
            Scenery.BANK_BOOTH_12801, Scenery.BANK_BOOTH_14367, Scenery.BANK_BOOTH_14368, Scenery.BANK_BOOTH_16700,
            Scenery.BANK_BOOTH_18491, Scenery.BANK_BOOTH_19230, Scenery.BANK_BOOTH_20325, Scenery.BANK_BOOTH_20326,
            Scenery.BANK_BOOTH_20327, Scenery.BANK_BOOTH_20328, Scenery.BANK_BOOTH_22819, Scenery.BANK_BOOTH_24914,
            Scenery.BANK_BOOTH_25808, Scenery.BANK_BOOTH_26972, Scenery.BANK_BOOTH_29085, Scenery.BANK_BOOTH_30015,
            Scenery.BANK_BOOTH_30016, Scenery.BANK_BOOTH_34205, Scenery.BANK_BOOTH_34752, Scenery.BANK_BOOTH_35647,
            Scenery.BANK_BOOTH_35648, Scenery.BANK_BOOTH_36262, Scenery.BANK_BOOTH_36786, Scenery.BANK_BOOTH_37474
        )
    }

    private fun tryInvokeBankerDialogue(player: Player, node: Node) {
        val boothLocation = node.location
        val playerLocation = player.location

        // TODO: Fix NPCs larger than 1 tile.
        // Fuck you, Vexia. Never show up whenever I'm around.
        Repository.findNPC(
            boothLocation.transform(
                boothLocation.x - playerLocation.x,
                boothLocation.y - playerLocation.y,
                0
            )
        )?.let {
            if (DialogueInterpreter.contains(it.id)) {
                it.faceLocation(node.location)
                openDialogue(player, it.id, NPC(it.id, it.location))
            } else {
                player.dialogueInterpreter.open(NPCs.BANKER_494)
            }
        }
    }

    private fun quickBankBoothUse(player: Player, node: Node): Boolean {
        if (player.ironmanManager.checkRestriction(IronmanMode.ULTIMATE)) {
            return true
        }

        if (BankerNPC.checkLunarIsleRestriction(player, node)) {
            tryInvokeBankerDialogue(player, node)
            return true
        }

        player.bank.open()
        return true
    }

    private fun regularBankBoothUse(player: Player, node: Node): Boolean {
        if (player.ironmanManager.checkRestriction(IronmanMode.ULTIMATE)) {
            return true
        }

        if (ServerConstants.BANK_BOOTH_QUICK_OPEN) {
            return quickBankBoothUse(player, node)
        }

        tryInvokeBankerDialogue(player, node)
        return true
    }

    private fun collectBankBoothUse(player: Player, node: Node): Boolean {
        if (BankerNPC.checkLunarIsleRestriction(player, node)) {
            tryInvokeBankerDialogue(player, node)
            return true
        }

        GrandExchangeRecords.getInstance(player).openCollectionBox()
        return true
    }

    override fun defineListeners() {
        on(BANK_BOOTHS, SCENERY, "use-quickly", "bank", handler = ::quickBankBoothUse)
        on(BANK_BOOTHS, SCENERY, "use", handler = ::regularBankBoothUse)
        on(BANK_BOOTHS, SCENERY, "collect", handler = ::collectBankBoothUse)
    }
}