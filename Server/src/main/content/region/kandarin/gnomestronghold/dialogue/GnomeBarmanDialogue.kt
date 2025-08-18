package content.region.kandarin.gnomestronghold.dialogue

import core.api.addItem
import core.api.inInventory
import core.api.openDialogue
import core.api.openNpcShop
import core.api.removeItem
import core.api.sendMessage
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

private const val INGREDIENT_COST = 20
private const val NOT_ENOUGH_MONEY_MESSAGE = "You do not have enough money to buy that."

/**
 * Handles the dialogue for the Gnome Barman NPCs in the Tree Gnome Stronghold.
 * @author Broseki
 */
@Initializable
class GnomeBarmanDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return GnomeBarmanDialogue(player)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, GnomeBarmanDialogueStart(), npc)
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARMAN_849)
    }
}

class GnomeBarmanDialogueStart : DialogueLabeller() {
    override fun addConversation() {
        npc(ChatAnim.OLD_HAPPY, "Good day to you. What can I get you to drink?")
        options(
            DialogueOption("store", "What do you have?", expression = ChatAnim.FRIENDLY, spokenText = "What do you have."),
            DialogueOption("nothing", "Nothing thanks.", expression = ChatAnim.FRIENDLY),
            DialogueOption(
                "ingredients", "Can I buy some ingredients?",
                spokenText = "I was just wanting to buy a cocktail ingredient actually.",
                expression = ChatAnim.FRIENDLY
            ),
        )

        label("store")
        npc(ChatAnim.OLD_HAPPY, "Here, take a look at our menu.")
        exec { player, npc ->
            openNpcShop(player, npc.id)
        }

        label("nothing")
        npc(ChatAnim.OLD_HAPPY, "Okay, take it easy.")

        label("ingredients")
        npc(ChatAnim.OLD_HAPPY, "Sure thing, what did you want?")
        options(
            DialogueOption("lemon", "A lemon.", expression = ChatAnim.FRIENDLY),
            DialogueOption("orange", "An orange.", expression = ChatAnim.FRIENDLY),
            DialogueOption("shaker", "A cocktail shaker.", expression = ChatAnim.FRIENDLY),
            DialogueOption(
                "buynothing", "Nothing thanks.",
                spokenText = "Actually nothing thanks.",
                expression = ChatAnim.FRIENDLY
            ),
        )

        label("lemon")
        npc(ChatAnim.OLD_HAPPY, "$INGREDIENT_COST coins please.")
        exec { player, _ ->
            if (removeItem(player, Item(Items.COINS_995, INGREDIENT_COST))) {
                addItem(player, Items.LEMON_2102)
            } else {
                sendMessage(player, NOT_ENOUGH_MONEY_MESSAGE)
            }
        }

        label("orange")
        npc(ChatAnim.OLD_HAPPY, "$INGREDIENT_COST coins please.")
        exec { player, _ ->
            if (removeItem(player, Item(Items.COINS_995, INGREDIENT_COST))) {
                addItem(player, Items.ORANGE_2108)
            } else {
                sendMessage(player, NOT_ENOUGH_MONEY_MESSAGE)
            }
        }

        label("shaker")
        npc(ChatAnim.OLD_HAPPY, "$INGREDIENT_COST coins please.")
        exec { player, _ ->
            if (removeItem(player, Item(Items.COINS_995, INGREDIENT_COST))) {
                addItem(player, Items.COCKTAIL_SHAKER_2025)
            } else {
                sendMessage(player, NOT_ENOUGH_MONEY_MESSAGE)
            }
        }

        label("buynothing")
        // Just end the conversation here without any further dialogue
    }
}