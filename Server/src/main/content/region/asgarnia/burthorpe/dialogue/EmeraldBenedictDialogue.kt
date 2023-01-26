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
 * Handles Emerald Benedict's dialogue tree.
 *
 * @author vddCore
 */
@Initializable
class EmeraldBenedictDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> if(hasIronmanRestriction(player, IronmanMode.ULTIMATE)) {
                npcl(
                    core.game.dialogue.FacialExpression.ANNOYED,
                    "Get lost, tin can."
                ).also { stage = END_DIALOGUE }
            } else {
                npcl(
                    core.game.dialogue.FacialExpression.SUSPICIOUS,
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
                core.game.dialogue.FacialExpression.SUSPICIOUS,
                "By the way, a little bird told me you got some stuff waiting for you " +
                "on the Grand Exchange."
            ).also { stage++ }

            2 -> showTopics(
                Topic(core.game.dialogue.FacialExpression.ASKING, "Yes, actually. Can you help?", 3),
                IfTopic(
                    core.game.dialogue.FacialExpression.ASKING,
                    "Yes, but can you switch my bank accounts?",
                    4,
                    hasActivatedSecondaryBankAccount(player)
                ),
                Topic(core.game.dialogue.FacialExpression.ASKING, "Yes, but can you show me my PIN settings?", 5),
                Topic(core.game.dialogue.FacialExpression.ASKING, "Yes, but can you show me my collection box?", 6),
                Topic(core.game.dialogue.FacialExpression.ANNOYED, "Yes, thanks. And I'll keep hold of it too.", END_DIALOGUE)
            )

            3 -> {
                openBankAccount(player)
                end()
            }

            4 -> {
                toggleBankAccount(player)
                npcl(
                    core.game.dialogue.FacialExpression.SUSPICIOUS,
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