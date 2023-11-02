package content.region.morytania.canifis.dialogue

import content.region.morytania.quest.creatureoffenkenstrain.RoavarDialogueFile
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Roavar dialogue.
 */
@Initializable
class RoavarDialogue (player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FacialExpression.HALF_GUILTY, "Hello there!")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                npc(FacialExpression.HALF_GUILTY, "Greeting traveller. Welcome to 'The Hair Of The Dog'", "Tavern. What can I do you for?")
                stage = 1
            }

            1 -> showTopics(
                    Topic<Int?>(FacialExpression.HALF_GUILTY, "Can I buy a beer?", 10, false),
                    Topic<Int?>(FacialExpression.HALF_GUILTY, "Can I hear some gossip", 20, false),
                    IfTopic<RoavarDialogueFile?>(FacialExpression.HALF_GUILTY, "Can I buy something to eat?", RoavarDialogueFile(1), player.getQuestRepository().getQuest("Creature of Fenkenstrain").getStage(player) == 2, false),
                    Topic<Int?>(FacialExpression.HALF_GUILTY, "Nothing thanks.", 40, false)
            )

            10 -> {
                npc(FacialExpression.HALF_GUILTY, "Well that's my speciality! The local brew's named", "'Moonlight Mead' and will set you back 5 gold.", "Waddya say? Fancy a pint?")
                stage = 11
            }

            11 -> {
                interpreter.sendOptions("Select an Option", "Yes please.", "Actually, no thanks.")
                stage = 12
            }

            12 -> when (buttonId) {
                1 -> {
                    player(FacialExpression.HALF_GUILTY, "Yes please.")
                    stage = 15
                }

                2 -> {
                    player(FacialExpression.HALF_GUILTY, "Actually, no thanks.")
                    stage = 14
                }
            }
            14 -> end()
            15 -> if (inInventory(player, Items.COINS_995, 5)) {
                if (removeItem(player, Item(Items.COINS_995, 5))) {
                    addItemOrDrop(player, Items.MOONLIGHT_MEAD_2955)
                    npc(FacialExpression.HALF_GUILTY, "Here ya go pal. Enjoy!")
                    stage = 16
                }
            } else {
                end()
                sendMessage(player, "You need 5 gold coins to buy a pint of beer.")
            }

            16 -> end()
            20 -> {
                npc(FacialExpression.HALF_GUILTY, "I am not one to gossip!")
                stage = 21
            }

            21 -> end()
            30 -> stage = if (inInventory(player, 2963, 1)) {
                npc(FacialExpression.HALF_GUILTY, "I don't have a spare lying around, sorry friend.", "Hopefully you'll find something else that can protect you", "against ghasts!")
                31
            } else {
                npc(FacialExpression.HALF_GUILTY, "Ah, well I do have one lying around.", "I suppose you could have it.")
                42
            }

            31 -> end()
            40 -> {
                npc(FacialExpression.HALF_GUILTY, "...I don't know why you talked to me if you don't want", "anything then...")
                stage = 41
            }

            41 -> end()
            42 -> {
                if (freeSlots(player) < 1) {
                    npc(FacialExpression.HALF_GUILTY, "Oh, nevermind. It seems your backpack is full.")
                } else {
                    sendDialogue(player, "The bartender hands you a silver sickle.")
                    addItem(player, 2963)
                }
                stage = 31
            }
        }
        return true
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return RoavarDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ROAVAR_1042)
    }
}
