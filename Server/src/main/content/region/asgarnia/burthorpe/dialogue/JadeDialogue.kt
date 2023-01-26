package content.region.asgarnia.burthorpe.dialogue

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

/**
 * Provides a dialogue tree for Jade inside Warriors' Guild.
 *
 * @author vddCore
 */
@Initializable
class JadeDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> if (hasIronmanRestriction(player, IronmanMode.ULTIMATE)) {
                npcl(
                    core.game.dialogue.FacialExpression.NEUTRAL,
                    "Greetings, warrior. I wish I could help you, but " +
                    "our services are not available for Ultimate ${if (player.isMale) "Ironmen" else "Ironwomen"}."
                ).also { stage = END_DIALOGUE }
            }
            else {
                npcl(
                    core.game.dialogue.FacialExpression.NEUTRAL,
                    "Greetings warrior, how may I help you?"
                ).also {
                    if (hasAwaitingGrandExchangeCollections(player)) {
                        stage++
                    } else {
                        stage += 2
                    }
                }
            }

            1 -> npcl(
                core.game.dialogue.FacialExpression.NEUTRAL,
                "Before we go any further, I should inform you that you " +
                "have items ready for collection from the Grand Exchange."
            ).also { stage++ }

            2 -> showTopics(
                Topic(core.game.dialogue.FacialExpression.NEUTRAL, "I'd like to access my bank account, please.", 10),
                IfTopic(
                    core.game.dialogue.FacialExpression.NEUTRAL,
                    "I'd like to switch to my ${getBankAccountName(player, true)} bank account.",
                    13,
                    hasActivatedSecondaryBankAccount(player)
                ),
                Topic(core.game.dialogue.FacialExpression.NEUTRAL, "I'd like to check my PIN settings.", 11),
                Topic(core.game.dialogue.FacialExpression.NEUTRAL, "I'd like to see my collection box.", 12),
                Topic(core.game.dialogue.FacialExpression.ASKING, "How long have you worked here?", 3)
            )

            3 -> npcl(
                core.game.dialogue.FacialExpression.FRIENDLY,
                "Oh, ever since the Guild opened. I like it here."
            ).also { stage++ }

            4 -> playerl(
                core.game.dialogue.FacialExpression.ASKING,
                "Why's that?"
            ).also { stage++ }

            5 -> npcl(
                core.game.dialogue.FacialExpression.FRIENDLY,
                "Well... What with all these warriors around, there's not much chance of my bank being robbed, is there?"
            ).also { stage = 2 }

            10 -> {
                openBankAccount(player)
                end()
            }

            11 -> {
                openBankPinSettings(player)
                end()
            }

            12 -> {
                openGrandExchangeCollectionBox(player)
                end()
            }

            13 -> {
                toggleBankAccount(player)
                npcl(
                    core.game.dialogue.FacialExpression.FRIENDLY,
                    "Of course! Your ${getBankAccountName(player)} account is now active!"
                ).also { stage = END_DIALOGUE }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.JADE_4296)
}