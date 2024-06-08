package content.region.asgarnia.rimmington.quest.witchpotion

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.item.Item
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE
import org.rs09.consts.Items

/**
 * Handles Hetty's dialogue for the Witch's Potion Quest.
 */
class HettyWitchsPotionDialogue(private val dStage: Int) : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (dStage) {
            0 -> handleQuestStartDialogue(player)
            20 -> handleGiveItemsDialogue(player)
            40 -> npcl(FacialExpression.ANNOYED, "Well are you going to drink the potion or not?").also { stage = END_DIALOGUE }
        }
    }

    private fun handleQuestStartDialogue(player: Player?) {
        player ?: return
        when (stage) {
            0 -> npcl(FacialExpression.HAPPY, "Ok I'm going to make a potion to help bring out your darker self.").also { stage++ }
            1 -> npcl(FacialExpression.NEUTRAL, "You will need certain ingredients.").also { stage++ }
            2 -> playerl(FacialExpression.NEUTRAL, "What do I need?").also { stage++ }
            3 -> npcl(FacialExpression.NEUTRAL, "You need an eye of newt, a rat's tail, an onion... Oh and a piece of burnt meat.").also { stage++ }
            4 -> {
                playerl(FacialExpression.HAPPY, "Great, I'll go and get them.")
                startQuest(player, WitchsPotion.QUEST_NAME)
                setQuestStage(player, WitchsPotion.QUEST_NAME, 20)
                stage = END_DIALOGUE
            }
        }
    }

    private fun handleGiveItemsDialogue(player: Player?) {
        player ?: return
        when (stage) {
            0 -> npcl(FacialExpression.HAPPY, "So have you found the things for the potion?").also { stage++ }
            1 -> {
                if (inInventory(player, Items.ONION_1957, 1) &&
                        inInventory(player, Items.RATS_TAIL_300, 1) &&
                        inInventory(player, Items.BURNT_MEAT_2146, 1) &&
                        inInventory(player, Items.EYE_OF_NEWT_221, 1)) {
                    playerl(FacialExpression.HAPPY, "Yes I have everything!").also { stage = 20 }
                } else {
                    playerl(FacialExpression.HALF_GUILTY, "I'm afraid I don't have all of them yet.").also { stage = 10 }
                }
            }

            10 -> npcl(FacialExpression.ANNOYED, "Well I can't make the potion without them! Remember...").also { stage++ }
            11 -> npcl(FacialExpression.NEUTRAL, "You need an eye of newt, a rat's tail, an onion, and a piece of burnt meat.").also { stage++ }
            12 -> npcl(FacialExpression.FRIENDLY, "Off you go dear!").also { stage = END_DIALOGUE }

            20 -> npcl(FacialExpression.HAPPY, "Excellent, can I have them then?").also { stage++ }
            21 -> sendDialogue(player, "You pass the ingredients to Hetty and she puts them all into her cauldron. Hetty closes her eyes and begins to chant. The cauldron bubbles mysteriously.").also { stage++ }

            22 -> playerl(FacialExpression.NEUTRAL, "Well, is it ready?").also { stage++ }
            23 -> {
                // Removing the items at this stage is authentic behavior
                if (removeItem(player, Item(Items.ONION_1957, 1)) &&
                        removeItem(player, Item(Items.RATS_TAIL_300, 1)) &&
                        removeItem(player, Item(Items.BURNT_MEAT_2146, 1)) &&
                        removeItem(player, Item(Items.EYE_OF_NEWT_221, 1))) {
                    npcl(FacialExpression.HAPPY, "Ok, now drink from the cauldron.")
                    setQuestStage(player, WitchsPotion.QUEST_NAME, 40)
                    stage = END_DIALOGUE
                }
            }
        }
    }
}
