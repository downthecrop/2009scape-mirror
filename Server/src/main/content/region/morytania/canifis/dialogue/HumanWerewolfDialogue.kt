package content.region.morytania.canifis.dialogue

import core.api.anyInEquipment
import core.api.toIntArray
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items

@Initializable
class HumanWerewolfDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage){
            START_DIALOGUE -> {
                // There are 10 random different messages for all of the werewolves in human form
                // If you have the Ring of Charos they think you're a werewolf and talk differently
                if (anyInEquipment(player, Items.RING_OF_CHAROS_4202, Items.RING_OF_CHAROSA_6465)){
                    // Nice talks
                    when ((1..10).random()){
                        1 -> npcl(FacialExpression.HAPPY, "I bet you have wonderful paws.").also { stage = END_DIALOGUE }
                        2 -> npcl(FacialExpression.NEUTRAL, "A very miserable day, altogether... enjoy it while it lasts.").also { stage = END_DIALOGUE }
                        3 -> npcl(FacialExpression.ASKING, "If you catch anyone promise me you'll share.").also { stage = END_DIALOGUE }
                        4 -> npcl(FacialExpression.ASKING, "I haven't smelt you around here before...").also { stage = END_DIALOGUE }
                        5 -> npcl(FacialExpression.FRIENDLY, "You smell familiar...").also { stage = END_DIALOGUE }
                        6 -> npcl(FacialExpression.ASKING, "Seen any humans around here? I'm v-e-r-y hungry.").also { stage = END_DIALOGUE }
                        7 -> npcl(FacialExpression.FRIENDLY, "You look to me like someone with a healthy taste for blood.").also { stage = END_DIALOGUE }
                        8 -> npcl(FacialExpression.FRIENDLY, "Good day to you, my friend.").also { stage = END_DIALOGUE }
                        9 -> npcl(FacialExpression.ASKING, "Fancy going up to the castle for a bit of a snack?").also { stage = END_DIALOGUE }
                        10 -> npcl(FacialExpression.NEUTRAL, "Give me a moment, I have a bit of someone stuck in my teeth...").also { stage = END_DIALOGUE }
                    }
                }
                else {
                    // Mean talks
                    when ((1..10).random()){
                        1 -> npcl(FacialExpression.ANNOYED, "If I were as ugly as you I would not dare to show my face in public!").also { stage = END_DIALOGUE }
                        2 -> npcl(FacialExpression.ANGRY, "Out of my way, punk.").also { stage = END_DIALOGUE }
                        // The only one that has a path
                        3 -> npcl(FacialExpression.ASKING, "Hmm... you smell strange...").also { stage++ }
                        4 -> npcl(FacialExpression.ANGRY, "Leave me alone.").also { stage = END_DIALOGUE }
                        5 -> npcl(FacialExpression.ANNOYED, "Don't talk to me again if you value your life!").also { stage = END_DIALOGUE }
                        6 -> npcl(FacialExpression.ANNOYED, "Get lost!").also { stage = END_DIALOGUE }
                        7 -> npcl(FacialExpression.ANNOYED, "I don't have anything to give you so leave me alone, mendicant.").also { stage = END_DIALOGUE }
                        8 -> npcl(FacialExpression.ANGRY, "Have you no manners?").also { stage = END_DIALOGUE }
                        9 -> npcl(FacialExpression.ANNOYED, "I don't have time for this right now.").also { stage = END_DIALOGUE }
                        10 -> npcl(FacialExpression.ANGRY, "I have no interest in talking to a pathetic meat bag like yourself.").also{ stage = END_DIALOGUE}
                    }
                }
            }
            // There's one path that the player can respond to (3 without the ring)
            1 -> playerl(FacialExpression.ASKING, "Strange how?").also { stage++ }
            2 -> npcl(FacialExpression.EVIL_LAUGH, "Like a human!").also { stage++ }
            3 -> playerl(FacialExpression.PANICKED, "Oh! Er... I just ate one is why!").also { stage = END_DIALOGUE }
            else -> {
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return (6026..6046).toIntArray()
    }
}