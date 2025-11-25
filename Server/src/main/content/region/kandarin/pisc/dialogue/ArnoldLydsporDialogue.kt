package content.region.kandarin.pisc.dialogue

import core.ServerConstants
import core.api.addItemOrDrop
import core.api.hasActivatedSecondaryBankAccount
import core.api.hasIronmanRestriction
import core.api.isUsingSecondaryBankAccount
import core.api.openBankAccount
import core.api.openBankPinSettings
import core.api.openGrandExchangeCollectionBox
import core.api.openNpcShop
import core.api.toggleBankAccount
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.IronmanMode
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class ArnoldLydsporDialogue : InteractionListener {
    override fun defineListeners() {
        on(NPCs.ARNOLD_LYDSPOR_3824, IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, ArnoldLydsporLabellerFile(), node as NPC)
            return@on true
        }
        on(NPCs.ARNOLD_LYDSPOR_3824, IntType.NPC, "bank") { player, _ ->
            openBankAccount(player)
            return@on true
        }
        on(NPCs.ARNOLD_LYDSPOR_3824, IntType.NPC, "collect") { player, _ ->
            openGrandExchangeCollectionBox(player)
            return@on true
        }
    }

    class ArnoldLydsporLabellerFile : DialogueLabeller() {
        override fun addConversation() {
            npc(ChatAnim.FRIENDLY, "Ah, you come back! What you want from Arnold, heh?")
            goto("main options")

            label("main options")
            options(
                DialogueOption("access", "Can you open my bank account please?", expression = ChatAnim.ASKING) { player, _ -> !hasIronmanRestriction(player, IronmanMode.ULTIMATE) },
                DialogueOption("buy second bank", "I'd like to open a secondary bank account.") { player, _ -> return@DialogueOption !hasIronmanRestriction(player, IronmanMode.ULTIMATE) && ServerConstants.SECOND_BANK && !hasActivatedSecondaryBankAccount(player) },
                DialogueOption("switch second bank", "I'd like to switch to my primary bank account.") { player, _ -> return@DialogueOption hasActivatedSecondaryBankAccount(player) && isUsingSecondaryBankAccount(player) },
                DialogueOption("switch second bank", "I'd like to switch to my secondary bank account.") { player, _ -> return@DialogueOption hasActivatedSecondaryBankAccount(player) && !isUsingSecondaryBankAccount(player) },
                DialogueOption("pin", "I'd like to check my PIN settings.", expression = ChatAnim.NEUTRAL) { player, _ -> !hasIronmanRestriction(player, IronmanMode.ULTIMATE) },
                DialogueOption("collect", "I'd like to collect items.", expression = ChatAnim.NEUTRAL) { player, _ -> !hasIronmanRestriction(player, IronmanMode.STANDARD) },
                DialogueOption("trade", "Would you like to trade?", expression = ChatAnim.ASKING),
                DialogueOption("chat", "Nothing, I just came to chat.", expression = ChatAnim.FRIENDLY) { player, _ -> return@DialogueOption hasIronmanRestriction(player, IronmanMode.STANDARD) || !ServerConstants.SECOND_BANK }
            )

            label("access")
            exec { player, _ -> openBankAccount(player) }
            goto("nowhere")

            label("buy second bank")
            npc(ChatAnim.GUILTY, "I'm so sorry! My little shop does not have the fibre-optic connections to be able to open a second account. Try asking again in a brick-and-mortar building in a big city!")
            player(ChatAnim.FRIENDLY, "That's okay, I understand. I will ask again in a big bank like Varrock's.")
            goto("nowhere")

            label("switch second bank")
            exec {
                player, _ -> toggleBankAccount(player)
                loadLabel(player, if (isUsingSecondaryBankAccount(player)) "now secondary" else "now primary")
            }
            label("now primary")
                npc("Your active bank account has been switched. You can now access your primary account.")
                goto("main options")
            label("now secondary")
                npc("Your active bank account has been switched. You can now access your secondary account.")
                goto("main options")

            label("pin")
            exec { player, _ -> openBankPinSettings(player) }
            goto("nowhere")

            label("collect")
            exec { player, _ -> openGrandExchangeCollectionBox(player) }
            goto("nowhere")

            label("trade")
            npc(ChatAnim.FRIENDLY, "Ja, I have wide range of stock...")
            exec { player, _ -> openNpcShop(player, NPCs.ARNOLD_LYDSPOR_3824) }
            goto("nowhere")

            label("chat")
            npc("Ah, that is nice - always I like to chat, but Herr Caranos tell me to get back to work! Here, you been nice, so have a present.")
            exec { player, _ -> addItemOrDrop(player, Items.CABBAGE_1965) }
            item(Item(Items.CABBAGE_1965), "Arnold gives you a cabbage.")
            player(ChatAnim.HALF_THINKING, "A cabbage?")
            npc(ChatAnim.HAPPY, "Ja, cabbage is good for you!")
            player(ChatAnim.NEUTRAL, "Um... Thanks!")
            goto("nowhere")
        }
    }
}
