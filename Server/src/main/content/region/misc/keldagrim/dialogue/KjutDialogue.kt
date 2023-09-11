package content.region.misc.keldagrim.dialogue

import core.api.addItemOrDrop
import core.api.inInventory
import core.api.isQuestComplete
import core.api.removeItem
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
class KjutDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE -> npcl(FacialExpression.OLD_DEFAULT, "Kebabs! Get your kebabs here! One gold each!").also { stage++ }
            1 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "Sure, one kebab please.", 2, true),
                    Topic(FacialExpression.FRIENDLY, "I'm looking for a drunken dwarf.", 6),
                    Topic(FacialExpression.FRIENDLY, "Do you have any quests?", 17),
            )
            2 -> {
                if (!inInventory(player, Items.COINS_995, 1)) {
                    playerl(FacialExpression.FRIENDLY, "Sure, one kebab please.").also { stage = 3 }
                } else {
                    if (removeItem(player, Item(Items.COINS_995, 1))) {
                        addItemOrDrop(player, Items.KEBAB_1971)
                        playerl(FacialExpression.FRIENDLY, "Sure, one kebab please.").also { stage = END_DIALOGUE }
                    }
                }
            }
            3 -> npcl(FacialExpression.OLD_ANGRY1, "Yes? Are you going to give me that one gold coin then?").also { stage++ }
            4 -> playerl(FacialExpression.FRIENDLY, "Sorry, I don't seem to have any money on me.").also { stage++ }
            5 -> npcl(FacialExpression.OLD_DEFAULT, "The bank is Keldagrim-West. Hurry back!").also {
                stage = END_DIALOGUE
            }
            6 -> {
                if (isQuestComplete(player, "Forgettable Tale of a Drunken Dwarf")) {
                    npcl(FacialExpression.OLD_DEFAULT, "I thought you would know plenty!").also { stage = 14 }
                } else {
                    npcl(FacialExpression.OLD_DEFAULT, "Just go out in the streets, they can't be hard to find!").also { stage = 7 }
                }
            }
            7 -> playerl(FacialExpression.FRIENDLY, "No, a specific drunken dwarf, he keeps harassing me.").also { stage++ }
            8 -> npcl(FacialExpression.OLD_DEFAULT, "That's still not very helpful. But let me see. There is one particularly drunken dwarf just to the northeast of here.").also { stage++ }
            9 -> npcl(FacialExpression.OLD_DEFAULT, "He's scaring away my customers, he says there's something wrong with my kebabs. Quite annoying fellow.").also { stage++ }
            10 -> playerl(FacialExpression.FRIENDLY, "I don't think that's who I mean.").also { stage++ }
            11 -> npcl(FacialExpression.OLD_DEFAULT, "Well, he does have a relative who wanders all over Gielinor. Comes back from time to time to buy more kebabs. Don't see him very often though.").also { stage++ }
            12 -> playerl(FacialExpression.FRIENDLY, "Do you think I'll have any chance of catching him here?").also { stage++ }
            13 -> npcl(FacialExpression.OLD_DEFAULT, "I think he was back here quite recently, so I doubt he'll be back any time soon.").also {
                stage = END_DIALOGUE
            }
            14 -> playerl(FacialExpression.FRIENDLY, "How do you mean?").also { stage++ }
            15 -> npcl(FacialExpression.OLD_DEFAULT, "Oh come on! You in the Laughing Miner? The whole city knows about you and your dwarven drinking buddies!").also { stage++ }
            16 -> playerl(FacialExpression.FRIENDLY, "Right, right.").also {
                stage = END_DIALOGUE
            }
            17 -> npcl(FacialExpression.OLD_DEFAULT, "Not really! I have a steady supply of meat coming in through the carts and I don't really need anything else!").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return KjutDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KJUT_2198)
    }
}