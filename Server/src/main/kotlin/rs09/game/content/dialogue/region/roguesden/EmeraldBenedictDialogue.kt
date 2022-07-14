package rs09.game.content.dialogue.region.roguesden

import api.*
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
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
            START_DIALOGUE -> if(hasIronmanRestriction(player, IronmanMode.ULTIMATE)) {
                npcl(
                    FacialExpression.ANNOYED,
                    "Get lost, tin can."
                ).also { stage = END_DIALOGUE }
            } else {
                npcl(
                    FacialExpression.SUSPICIOUS,
                    "Got anything you don't want to lose?"
                ).also {
                    if (hasAwaitingGrandExchangeCollections(player)) {
                        stage++
                    } else {
                        stage += 2
                    }
                }
            }

            1 -> npcl(
                FacialExpression.SUSPICIOUS,
                "By the way, a little bird told me you got some stuff waiting for you " +
                "on the Grand Exchange."
            ).also { stage++ }

            2 -> showTopics(
                Topic(FacialExpression.ASKING, "Yes, actually. Can you help?", 3),
                IfTopic(
                    FacialExpression.ASKING,
                    "Yes, but can you switch my bank accounts?",
                    4,
                    hasActivatedSecondaryBankAccount(player)
                ),
                Topic(FacialExpression.ASKING, "Yes, but can you show me my PIN settings?", 5),
                Topic(FacialExpression.ASKING, "Yes, but can you show me my collection box?", 6),
                Topic(FacialExpression.ANNOYED, "Yes, thanks. And I'll keep hold of it too.", END_DIALOGUE)
            )

            3 -> {
                openBankAccount(player)
                end()
            }

            4 -> {
                toggleBankAccount(player)
                npcl(
                    FacialExpression.SUSPICIOUS,
                    "Sure thing. Feel free to rummage through whatever's in your ${getBankAccountName(player)} now."
                ).also { stage = END_DIALOGUE }
            }

            5 -> {
                openBankPinSettings(player)
                end()
            }

            6 -> {
                openGrandExchangeCollectionBox(player)
                end()
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.EMERALD_BENEDICT_2271)
}