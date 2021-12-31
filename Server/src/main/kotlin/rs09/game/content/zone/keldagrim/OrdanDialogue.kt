package rs09.game.content.zone.keldagrim

import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import api.*
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.tools.END_DIALOGUE

/**It's just some simple Ordan dialogue
 * don't look at the unnoting code if you care
 * about your braincells
 * @author phil lips*/

const val ORDAN = 2564

@Initializable
class OrdanDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        sendPlayerDialogue(player,"Can you un-note any of my items?")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> sendNPCDialogue(player, ORDAN,"I can un-note Tin, Copper, Iron, Coal, and Mithril.").also { stage++ }
            1 -> sendNPCDialogue(player, ORDAN,"I can even un-note Adamantite and Runite, but you're gonna need deep pocktes for that.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return OrdanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(ORDAN)
    }

    /**This is for unnoting ores from Ordan, yeah I know being able to unnote Addy and Rune isn't authentic
     * but QOL wins out in this one, however unnoting Addy and Runite is expensive as fuck and is essentially
     * a big fat smelly gold sink, perhaps it will add some value to GP uh ha ha
     * just comment those options out if you don't want em
     * aka "WOW I CAN'T BELIEVE IT'S ALL CONDITIONALS*/

    class OrdanUnnoteListener : InteractionListener() {
        val notedOre = intArrayOf(
            Items.IRON_ORE_441,
            Items.COPPER_ORE_437,
            Items.TIN_ORE_439,
            Items.COAL_454,
            Items.MITHRIL_ORE_448,
            Items.ADAMANTITE_ORE_450,
            Items.SILVER_ORE_443,
            Items.GOLD_ORE_445,
            Items.RUNITE_ORE_452
        )
        val UNNOTE_PRICES = hashMapOf(
            Items.IRON_ORE_441 to 8,
            Items.COPPER_ORE_437 to 10,
            Items.TIN_ORE_439 to 10,
            Items.COAL_454 to 22,
            Items.MITHRIL_ORE_448 to 81,
            Items.ADAMANTITE_ORE_450 to 330,
            Items.SILVER_ORE_443 to 37,
            Items.GOLD_ORE_445 to 75,
            Items.RUNITE_ORE_452 to 1000
        )
        override fun defineListeners() {
            onUseWith(NPC, NPCs.ORDAN_2564,*notedOre){ player, _, noteType ->
                val noteAmount = amountInInventory(player, noteType.id)
                val noteName = noteType.name
                var unNoteAmount = 0
                var inventorySlots = freeSlots(player)
                var actualAmount = 0
                var proceed = false
                var wait = false
                player.dialogueInterpreter.sendOptions("$noteName un-note?","1","5","10","X")
                player.dialogueInterpreter.addAction { player, button ->
                    when(button) {
                        2 -> unNoteAmount = 1
                        3 -> unNoteAmount = 5
                        4 -> unNoteAmount = 10
                        5 ->{sendInputDialogue(player, true, "Enter amount:") { value ->
                                 unNoteAmount = value as Int
                            actualAmount = if(unNoteAmount > inventorySlots){
                                inventorySlots
                            }else{
                                unNoteAmount
                            }
                            if(actualAmount > noteAmount){
                                actualAmount = noteAmount
                            }
                            /**Yes this is a second when statement that does the exact same thing
                             * as the other one, and no im not removing this because if I do then
                             * custom input amounts don't work and im not spending more than
                             * a day making it work with just one when statement.*/
                            val totalPrice = UNNOTE_PRICES[noteType.id]!! * (actualAmount)
                            if(amountInInventory(player,Items.COINS_995) >= totalPrice && actualAmount > 0 ){
                                removeItem(player, Item(995,totalPrice),Container.INVENTORY)
                                removeItem(player, Item(noteType.id,actualAmount),Container.INVENTORY)
                                addItem(player,noteType.id - 1,actualAmount)
                            }
                            }
                        }
                    }
                    actualAmount = if(unNoteAmount > inventorySlots){
                        inventorySlots
                    }else{
                        unNoteAmount
                    }
                    if(actualAmount > noteAmount){
                        actualAmount = noteAmount
                    }
                    val totalPrice = UNNOTE_PRICES[noteType.id]!! * (actualAmount)
                    if(amountInInventory(player,Items.COINS_995) >= totalPrice && actualAmount > 0 ){
                        removeItem(player, Item(995,totalPrice),Container.INVENTORY)
                        removeItem(player, Item(noteType.id,actualAmount),Container.INVENTORY)
                        addItem(player,noteType.id - 1,actualAmount)
                    }
                }
                return@onUseWith true
            }
        }
    }
}