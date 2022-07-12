package rs09.game.content.dialogue.region.roguesden

import api.*
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.IfTopic
import rs09.game.content.dialogue.Topic
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

/**
 * Handles Emerald Benedict's dialogue tree.
 *
 * @author vddCore
 */
@Initializable
class EmeraldBenedictDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(
                FacialExpression.SUSPICIOUS,
                "Got anything you don't want to lose?"
            ).also { stage++ }

            1 -> showTopics(
                Topic(FacialExpression.ASKING, "Yes, actually. Can you help?", 2),
                IfTopic(
                    FacialExpression.ASKING,
                    "Yes, but can you switch my bank accounts?",
                    3,
                    hasActivatedSecondaryBankAccount(player)
                ),
                Topic(FacialExpression.ASKING, "Yes, but can you show me my PIN settings?", 4),
                Topic(FacialExpression.ASKING, "Yes, but can you show me my collection box?", 5),
                Topic(FacialExpression.ANNOYED, "Yes, thanks. And I'll keep hold of it too.", END_DIALOGUE)
            )

            2 -> {
                openBankAccount(player)
                end()
            }

            3 -> {
                toggleBankAccount(player)
                npcl(
                    FacialExpression.SUSPICIOUS,
                    "Sure thing. Feel free to rummage through whatever's in your ${getBankAccountName(player)}."
                ).also { stage = END_DIALOGUE }
            }

            4 -> {
                openBankPinSettings(player)
                end()
            }

            5 -> {
                openGrandExchangeCollectionBox(player)
                end()
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.EMERALD_BENEDICT_2271)
}