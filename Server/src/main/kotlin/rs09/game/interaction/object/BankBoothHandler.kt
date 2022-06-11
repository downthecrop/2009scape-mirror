package rs09.game.interaction.`object`

import api.toIntArray
import core.game.component.CloseEvent
import core.game.component.Component
import core.game.component.InterfaceType
import core.game.container.access.InterfaceContainer
import core.game.content.dialogue.DialogueInterpreter
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.Location
import org.rs09.consts.Components
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import rs09.ServerConstants
import rs09.game.ge.GrandExchangeRecords
import rs09.game.interaction.InteractionListener
import rs09.game.world.repository.Repository

private val BANK_BOOTHS = intArrayOf(
    Scenery.BANK_BOOTH_2213,
    Scenery.BANK_BOOTH_2214,
    Scenery.BANK_BOOTH_3045,
    Scenery.BANK_BOOTH_5276,
    Scenery.BANK_BOOTH_6084,
    Scenery.BANK_BOOTH_10517,
    Scenery.BANK_BOOTH_11338,
    Scenery.BANK_BOOTH_11402,
    Scenery.BANK_BOOTH_11758,
    Scenery.BANK_BOOTH_12798,
    Scenery.BANK_BOOTH_12799,
    Scenery.BANK_BOOTH_12800,
    Scenery.BANK_BOOTH_12801,
    Scenery.BANK_BOOTH_14367,
    Scenery.BANK_BOOTH_14368,
    Scenery.BANK_BOOTH_16700,
    Scenery.BANK_BOOTH_18491,
    Scenery.BANK_BOOTH_19230,
    Scenery.BANK_BOOTH_20325,
    Scenery.BANK_BOOTH_20326,
    Scenery.BANK_BOOTH_20327,
    Scenery.BANK_BOOTH_20328,
    Scenery.BANK_BOOTH_22819,
    Scenery.BANK_BOOTH_24914,
    Scenery.BANK_BOOTH_25808,
    Scenery.BANK_BOOTH_26972,
    Scenery.BANK_BOOTH_29085,
    Scenery.BANK_BOOTH_30015,
    Scenery.BANK_BOOTH_30016,
    Scenery.BANK_BOOTH_34205,
    Scenery.BANK_BOOTH_34752,
    Scenery.BANK_BOOTH_35647,
    Scenery.BANK_BOOTH_35648,
    Scenery.BANK_BOOTH_36262,
    Scenery.BANK_BOOTH_36786,
    Scenery.BANK_BOOTH_37474
)

/**
 * Allows the user to interact with bank booths.
 *
 * @author vddCore
 */
class BankBoothHandler : InteractionListener {

    private fun quickBankBoothUse(player: Player, node: Node): Boolean {
        if (player.ironmanManager.checkRestriction(IronmanMode.ULTIMATE)) {
            return true
        }

        player.bank.open()
        updateAchivementDiary(player);

        return true
        TODO("Rip this out and put it in a proper achivement diary listener.")
    }

    private fun regularBankBoothUse(player: Player, node: Node): Boolean {
        if (player.ironmanManager.checkRestriction(IronmanMode.ULTIMATE)) {
            return true
        }

        if (ServerConstants.BANK_BOOTH_QUICK_OPEN) {
            return quickBankBoothUse(player, node)
        }

        val boothLocation = node.location
        val playerLocation = player.location

        val bankerNPC = Repository.findNPC(
            boothLocation.transform(
                boothLocation.x - playerLocation.x,
                boothLocation.y - playerLocation.y,
                0
            )
        )

        if (bankerNPC != null && DialogueInterpreter.contains(bankerNPC.id)) {
            bankerNPC.faceLocation(node.location)
            player.dialogueInterpreter.open(bankerNPC.id, bankerNPC.id)
        } else {
            player.dialogueInterpreter.open(NPCs.BANKER_494)
        }

        return true
    }

    private fun collectBankBoothUse(player: Player, node: Node): Boolean {
        GrandExchangeRecords.getInstance(player).openCollectionBox()
        return true
    }

    // It makes no sense to have it here.
    private fun updateAchivementDiary(player: Player) {
        if (player.location.withinDistance(Location(3092, 3243, 0))) {
            player.achievementDiaryManager.finishTask(
                player,
                DiaryType.LUMBRIDGE,
                1,
                15
            )
        }
    }

    override fun defineListeners() {
        on(BANK_BOOTHS, SCENERY, "use-quickly", "bank", handler = ::quickBankBoothUse)
        on(BANK_BOOTHS, SCENERY, "use", handler = ::regularBankBoothUse)
        on(BANK_BOOTHS, SCENERY, "collect", handler = ::collectBankBoothUse)
    }
}