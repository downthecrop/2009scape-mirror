package content.region.misc.keldagrim.dialogue

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class BarmaidDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // TODO: Forgettable Tale Dialogue
        when(stage){
            START_DIALOGUE -> npcl(FacialExpression.OLD_DEFAULT, "Welcome to the Laughing Miner pub, human traveller.").also { stage++ }
            1 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "Who is that man walking around outside?", 2),
                    Topic(FacialExpression.FRIENDLY, "I'd like a beer please.", 12),
                    Topic(FacialExpression.FRIENDLY, "I'd like some food please.", 16),
            )
            2 -> npcl(FacialExpression.OLD_DEFAULT, "What man?").also { stage++ }
            3 -> playerl(FacialExpression.FRIENDLY, "I mean the dwarf, the one with the sign.").also { stage++ }
            4 -> npcl(FacialExpression.OLD_DEFAULT, "Oh, him. Yes, we employ him to advertise our pub, he's the cheapest labour we could find. We don't have a lot of money to spare you know, we pay him in beer.").also { stage++ }
            5 -> playerl(FacialExpression.FRIENDLY, "But what's wrong with him?").also { stage++ }
            6 -> npcl(FacialExpression.OLD_DEFAULT, "Well, he's drunk isn't he?").also { stage++ }
            7 -> npcl(FacialExpression.OLD_DEFAULT, "I told you, he's cheap. He couldn't get any other work since the Red Axe fired him. Been drinking ever since.").also { stage++ }
            8 -> playerl(FacialExpression.FRIENDLY, "How did that happen?").also { stage++ }
            9 -> npcl(FacialExpression.OLD_DEFAULT, "I'm not quite sure, I think he kept wearing the wrong coloured cap all the time. They don't much like it if you don't wear your red uniform into work.").also { stage++ }
            10 -> playerl(FacialExpression.FRIENDLY, "They fired him for that??").also { stage++ }
            11 -> npcl(FacialExpression.OLD_DEFAULT, "The Red Axe will fire you for just about anything if they want to.").also {
                stage = END_DIALOGUE
            }
            12 -> npcl(FacialExpression.OLD_DEFAULT, "That'll be 2 gold coins.").also { stage++ }
            13 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "Pay.", 14, true),
                    Topic(FacialExpression.FRIENDLY, "Don't pay.", 15,true),
            )
            14 -> {
                if (!inInventory(player, Items.COINS_995, 2)) {
                    playerl(FacialExpression.FRIENDLY, "Sorry, I don't have 2 coins on me.").also { stage = END_DIALOGUE }
                } else {
                    if (removeItem(player, Item(Items.COINS_995, 2))) {
                        addItemOrDrop(player, Items.BEER_1917)
                        npcl(FacialExpression.OLD_DEFAULT, "Thanks for your custom.").also { stage = END_DIALOGUE }
                    }
                }
            }
            15 -> playerl(FacialExpression.FRIENDLY, "Sorry, I changed my mind.").also { stage = END_DIALOGUE }
            16 -> npcl(FacialExpression.OLD_DEFAULT, "I can make you a stew for 20 gold coins.").also { stage++ }
            17 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "Pay.", 18, true),
                    Topic(FacialExpression.FRIENDLY, "Don't pay.", 19,true),
            )
            18 -> {
                if (!inInventory(player, Items.COINS_995, 20)) {
                    playerl(FacialExpression.FRIENDLY, "Sorry, I don't have 20 coins on me.").also { stage = END_DIALOGUE }
                } else {
                    if (removeItem(player, Item(Items.COINS_995, 20))) {
                        addItemOrDrop(player, Items.STEW_2003)
                        npcl(FacialExpression.OLD_DEFAULT, "Thanks for your custom.").also { stage = END_DIALOGUE }
                    }
                }
            }
            19 -> playerl(FacialExpression.FRIENDLY, "Sorry, I changed my mind.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return BarmaidDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARMAID_2178)
    }
}