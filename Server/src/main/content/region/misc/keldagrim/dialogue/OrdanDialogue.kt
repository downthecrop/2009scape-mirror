package content.region.misc.keldagrim.dialogue

import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items

@Initializable
class OrdanDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player), InteractionListener {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // Dialogue follows https://www.youtube.com/watch?v=mwoA01EYQks a 29 May 2010 capture
        when(stage){
            START_DIALOGUE -> npcl(FacialExpression.OLD_HAPPY, "Are you here to smith? Do you want to buy some ore?").also { stage++ }
            1 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "Yes please.", 2),
                    Topic(FacialExpression.FRIENDLY, "Can you un-note any of my items?", 3),
                    Topic(FacialExpression.FRIENDLY, "No thanks.", 5),
            )
            2 -> openNpcShop(player, NPCs.ORDAN_2564).also {
                end()
            }
            3 -> npc(FacialExpression.OLD_HAPPY,"I suppose I can un-note any ores that I sell in my", "shop - for a price of course. Just give me the noted ore", "you wish to exchange.").also {
                stage++
            }
            // This following dialogue is not authentic, but was originally here.
            4 -> npcl(FacialExpression.OLD_HAPPY,"I can even un-note Adamantite and Runite, but you're gonna need deep pockets for that.").also {
                stage = END_DIALOGUE
            }
            5 -> npcl(FacialExpression.OLD_ANGRY1, "Well don't waste my time then!").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return OrdanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ORDAN_2564)
    }

    override fun defineListeners() {
        onUseWith(IntType.NPC, OrdanDialogueFile.ORES_TO_UNNOTE_PRICES.keys.toIntArray(), NPCs.ORDAN_2564) { player, noteType, npc ->
            openDialogue(player, OrdanDialogueFile(noteType.id, noteType.name), npc.asNpc())
            return@onUseWith true
        }
    }
}

class OrdanDialogueFile(private val noteOreId: Int, private val noteOreName: String) : DialogueFile() {

    companion object {
        val ORES_TO_UNNOTE_PRICES = hashMapOf(
                Items.COPPER_ORE_437 to 10,
                Items.TIN_ORE_439 to 10,
                Items.IRON_ORE_441 to 8,
                Items.SILVER_ORE_443 to 37,
                Items.GOLD_ORE_445 to 75,
                Items.MITHRIL_ORE_448 to 81,
                Items.ADAMANTITE_ORE_450 to 330,
                Items.RUNITE_ORE_452 to 1000,
                Items.COAL_454 to 22,
        )
    }

    // These are instance variables for un-noting dialogue.
    var unnoteAmount = 0
    var unnotePrice = 0

    // Dialogue follows https://www.youtube.com/watch?v=nqwzco2Es7o a 6 Dec 2009 capture
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            START_DIALOGUE -> {
                // Swords position iface/child changes according to number of dialogue options.
                setComponentVisibility(player!!, 232, 9, false)
                setComponentVisibility(player!!, 232, 8, true)
                sendDialogueOptions(player!!, "How much $noteOreName would you like to un-note?", "1", "5", "10", "X")
                stage++
            }
            1 -> when (buttonID) {
                1 -> {
                    unnoteAmount = 1
                    unnotePrice = 1 * ORES_TO_UNNOTE_PRICES[noteOreId]!!
                    npcl(FacialExpression.OLD_HAPPY, "I can un-note those for $unnotePrice gold pieces, is that okay?").also { stage++ }
                }
                2 -> {
                    unnoteAmount = 5
                    unnotePrice = 5 * ORES_TO_UNNOTE_PRICES[noteOreId]!!
                    npcl(FacialExpression.OLD_HAPPY, "I can un-note those for $unnotePrice gold pieces, is that okay?").also { stage++ }
                }
                3 -> {
                    unnoteAmount = 10
                    unnotePrice = 10 * ORES_TO_UNNOTE_PRICES[noteOreId]!!
                    npcl(FacialExpression.OLD_HAPPY, "I can un-note those for $unnotePrice gold pieces, is that okay?").also { stage++ }
                }
                4 -> sendInputDialogue(player!!, true, "Enter amount:") { value ->
                    unnoteAmount = value as Int
                    // For QOL, reduce the amount to the number of free slots in player's inventory.
                    if (unnoteAmount > freeSlots(player!!)) {
                        unnoteAmount = freeSlots(player!!)
                    }
                    unnotePrice = unnoteAmount * ORES_TO_UNNOTE_PRICES[noteOreId]!!
                    // Dialogue is to cater to QOL
                    npcl(FacialExpression.OLD_HAPPY, "I can un-note $unnoteAmount $noteOreName for $unnotePrice gold pieces, is that okay?").also { stage++ }
                    return@sendInputDialogue
                }
            }
            2 -> showTopics(
                Topic(FacialExpression.FRIENDLY, "It's a deal.", 3),
                Topic(FacialExpression.FRIENDLY, "No, that's too expensive.", END_DIALOGUE),
            )
            3 -> {
                if (freeSlots(player!!) < unnoteAmount) {
                    npc(FacialExpression.OLD_NORMAL, "You don't have enough room in your inventory for that", "number of un-noted items. Clear some space and try", "again.").also { stage = END_DIALOGUE }
                } else if(amountInInventory(player!!, noteOreId) < unnoteAmount) {
                    sendDialogueLines(player!!, "You do not have enough notes to un-note", "$unnoteAmount $noteOreName.").also { stage = END_DIALOGUE }
                } else if(amountInInventory(player!!, Items.COINS_995) < unnotePrice){
                    sendDialogueLines(player!!, "You do not have enough coins to afford un-noting", "$unnoteAmount $noteOreName.").also{ stage = END_DIALOGUE }
                } else {
                    if(removeItem(player!!, Item(Items.COINS_995, unnotePrice), Container.INVENTORY)  && removeItem(player!!, Item(noteOreId, unnoteAmount), Container.INVENTORY)) {
                        addItem(player!!, noteOreId - 1, unnoteAmount)
                    }
                    end()
                }
            }
        }
    }
}