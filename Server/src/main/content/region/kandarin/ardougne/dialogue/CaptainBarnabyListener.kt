package content.region.kandarin.ardougne.dialogue

import content.global.travel.ship.Ships
import core.api.*
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs


class CaptainBarnabyListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.CAPTAIN_BARNABY_4974, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player,CaptainBarnabyDialogueFile(), npc)
            return@on true
        }
        on(NPCs.CAPTAIN_BARNABY_4974, IntType.NPC, "pay-fare") { player, _ ->
            if (removeItem(player,Item(Items.COINS_995,30))) {
                sail(player, true)
            } else {
                sendMessage(player,"You don't have enough coins.")
            }
            return@on true
        }
    }
}

class CaptainBarnabyDialogueFile : DialogueLabeller() {

    override fun addConversation() {

        label("start")
        npc("Do you want to go on a trip to Brimhaven?")
        npc("The trip will cost you 30 coins.")
        options(
            DialogueOption("pay_yes", "Yes please."),
            DialogueOption("end", "No, thank you."),
            DialogueOption("pay_charm", "[Charm] Or I could pay you nothing at all...",
                spokenText = "Or I could pay you nothing at all...") { player, _ ->
                inEquipment(player, Items.RING_OF_CHAROSA_6465)
            }
        )

        label("pay_yes")
        exec { player, _ ->
            if (removeItem(player, Item(Items.COINS_995, 30))) {
                sail(player, true)
                loadLabel(player, "end")
            } else {
                loadLabel(player, "no_coins")
            }
        }

        label("no_coins")
        player("Sorry, I don't seem to have enough coins.")
        goto("end")

        label("pay_charm")
        npc("Mmmm ... Nothing at all you say ...")
        npc("Yes, why not - jump aboard then.")
        exec { player, _ ->
            sail(player, false)
        }
        goto("end")
    }
}

private fun sail(player : Player, coins : Boolean) {
    if (coins) sendMessage(player, "You pay 30 coins and board the ship.")
    else sendMessage(player, "You board the ship.")

    playJingle(player, 171)
    Ships.ARDOUGNE_TO_BRIMHAVEN.sail(player)
}