package rs09.game.content.dialogue.region.bountyhunter

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
 * Provides dialogue tree for Maximillian Sackville,
 * the Bounty Hounter roving banker.
 *
 * @author vddCore
 */
@Initializable
class MaximillianSackvilleDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> when {
                hasIronmanRestriction(player, IronmanMode.ULTIMATE) -> {
                    npcl(
                        FacialExpression.NEUTRAL,
                        "My apologies, dear ${if (player.isMale) "sir" else "madam"}, " +
                        "our services are not available for Ultimate ${if (player.isMale) "Ironmen" else "Ironwomen"}."
                    ).also { stage = END_DIALOGUE }
                }

                else -> {
                    npcl(
                        FacialExpression.NEUTRAL,
                        "Good day, how may I help you?"
                    ).also {
                        if (hasAwaitingGrandExchangeCollections(player)) {
                            stage++
                        } else {
                            stage += 2
                        }
                    }
                }
            }

            1 -> npcl(
                FacialExpression.NEUTRAL,
                "Before we go any further, I should inform you that you " +
                "have items ready for collection from the Grand Exchange."
            ).also { stage++ }

            2 -> playerl(
                FacialExpression.ASKING,
                "Who are you?"
            ).also { stage++ }

            3 -> npcl(
                FacialExpression.NEUTRAL,
                "How inconsiderate of me, dear ${if (player.isMale) "sir" else "madam"}. " +
                "My name is Maximillian Sackville and I conduct operations here on behalf " +
                "of The Bank of Gielinor."
            ).also { stage++ }

            4 -> showTopics(
                Topic(FacialExpression.NEUTRAL, "I'd like to access my bank account.", 10),
                IfTopic(
                    FacialExpression.NEUTRAL,
                    "I'd like to switch to my ${getBankAccountName(player, true)} bank account.",
                    11,
                    hasActivatedSecondaryBankAccount(player)
                ),
                Topic(FacialExpression.NEUTRAL, "I'd like to check my PIN settings.", 12),
                Topic(FacialExpression.NEUTRAL, "I'd like to collect items.", 13),
                Topic(FacialExpression.ASKING, "Aren't you afraid of working in the Wilderness?", 5)
            )

            5 -> npcl(
                FacialExpression.NEUTRAL,
                "While the Wilderness is quite a dangerous place, The Bank of Gielinor offers " +
                "us - roving bankers - extraordinary benefits for our hard work in hazardous environments."
            ).also { stage++ }

            6 -> npcl(
                FacialExpression.NEUTRAL,
                "This allows us to provide our services to customers regardless of their current " +
                "whereabouts. Our desire to serve is stronger than our fear of the Wilderness."
            ).also { stage = END_DIALOGUE }

            10 -> {
                openBankAccount(player)
                end()
            }

            11 -> {
                toggleBankAccount(player)

                npcl(
                    FacialExpression.NEUTRAL,
                    "Naturally. You can now access your ${getBankAccountName(player)} bank account."
                ).also { stage = END_DIALOGUE }
            }

            12 -> {
                openBankPinSettings(player)
                end()
            }

            13 -> {
                openGrandExchangeCollectionBox(player)
                end()
            }
        }

        return true
    }

    override fun getIds() = intArrayOf(NPCs.BANKER_6538)
}