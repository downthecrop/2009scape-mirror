package content.region.kandarin.yanille.quest.thehandinthesand.dialogue

import core.api.addItemOrDrop
import core.api.inInventory
import core.api.openDialogue
import core.api.removeItem
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Dialogue for the Dragon Inn bartender in Yanille.
 * @author Edith
 */

@Initializable
class DragonInnBartenderDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return DragonInnBartenderDialogue(player)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, DragonInnBartenderDialogueFile(), npc)
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARTENDER_739)
    }
}

class DragonInnBartenderDialogueFile : DialogueLabeller() {
    private fun buyBeer(player: Player, itemId: Int, price: Int) {
        if (!inInventory(player, Items.COINS_995, price) || !removeItem(player, Item(Items.COINS_995, price))) {
            loadLabel(player, "has_no_money")
            return
        }

        addItemOrDrop(player, itemId)
    }

    override fun addConversation() {

        npc(FacialExpression.ASKING, "What can I get you?")
        player(FacialExpression.ASKING, "What's on the menu?")
        npc(FacialExpression.HAPPY, "Dragon Bitter and Greenman's Ale, oh and some cheap beer.")
        options(
            DialogueOption("give_it_miss", "I'll give it a miss I think.", "I'll give it a miss I think."),
            DialogueOption("buy_dragon_bitter", "I'll try the Dragon Bitter.", "I'll try the Dragon Bitter."),
            DialogueOption("buy_greenmans_ale", "Can I have some Greenman's Ale?", "Can I have some Greenman's Ale?", FacialExpression.ASKING),
            DialogueOption("buy_cheap_beer", "One cheap beer please!", "One cheap beer please!", FacialExpression.HAPPY)
        )

        label("give_it_miss")
            npc("Come back when you're a little thirstier.")

        label("buy_dragon_bitter")
            npc(FacialExpression.HAPPY, "Ok, that'll be two coins.")
            exec { player, _ ->
                buyBeer(player, Items.DRAGON_BITTER_1911, 2)
            }
            item(Item(Items.DRAGON_BITTER_1911), "You buy a pint of Dragon Bitter.")

        label("buy_greenmans_ale")
            npc("Ok, that'll be ten coins.")
            exec { player, _ ->
                buyBeer(player, Items.GREENMANS_ALE_1909, 10)
            }
            item(Item(Items.GREENMANS_ALE_1909), "You buy a pint of Greenman's Ale.")

        label("buy_cheap_beer")
            npc(FacialExpression.HAPPY, "That'll be 2 gold coins please!")
            exec { player, _ ->
                buyBeer(player, Items.BEER_1917, 2)
            }
            item(Item(Items.BEER_1917), "You buy a pint of cheap beer.")
            npc(FacialExpression.HAPPY, "Have a super day!")

        label("has_no_money")
            player("Oh dear. I don't seem to have enough money.")

    }

}