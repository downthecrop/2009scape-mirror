package content.region.kandarin.pisc.dialogue

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

/**
 * Provides the regular dialogue for Arnold Lydspor.
 * TODO: Swan Song quest will need a special case handling.
 *
 * @author vddCore
 */
@Initializable
class ArnoldLydsporDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(
                core.game.dialogue.FacialExpression.FRIENDLY,
                "Ah, you come back! What you want from Arnold, heh?"
            ).also { stage++ }

            1 -> showTopics(
                IfTopic(
                    core.game.dialogue.FacialExpression.ASKING,
                    "Can you open my bank account, please?",
                    2,
                    !hasIronmanRestriction(player, IronmanMode.ULTIMATE)
                ),

                IfTopic(
                    core.game.dialogue.FacialExpression.NEUTRAL,
                    "I'd like to check my bank PIN settings.",
                    3,
                    !hasIronmanRestriction(player, IronmanMode.ULTIMATE)
                ),
                IfTopic(
                    core.game.dialogue.FacialExpression.NEUTRAL,
                    "I'd like to collect items.",
                    4,
                    !hasIronmanRestriction(player, IronmanMode.ULTIMATE)
                ),
                Topic(core.game.dialogue.FacialExpression.ASKING, "Would you like to trade?", 5),
                Topic(core.game.dialogue.FacialExpression.FRIENDLY, "Nothing, I just came to chat.", 7)
            )

            2 -> {
                openBankAccount(player)
                end()
            }

            3 -> {
                openBankPinSettings(player)
                end()
            }

            4 -> {
                openGrandExchangeCollectionBox(player)
                end()
            }

            5 -> npcl(
                core.game.dialogue.FacialExpression.FRIENDLY,
                "Ja, I have wide range of stock..."
            ).also { stage++ }

            6 -> {
                openNpcShop(player, NPCs.ARNOLD_LYDSPOR_3824)
                end()
            }

            7 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY,
                "Ah, that is nice - always I like to chat, but " +
                "Herr Caranos tell me to get back to work! " +
                "Here, you been nice, so have a present."
            ).also { stage++ }

            8 -> sendItemDialogue(
                player,
                Items.CABBAGE_1965,
                "Arnold gives you a cabbage."
            ).also {
                addItemOrDrop(player, Items.CABBAGE_1965)
                stage++
            }

            9 -> playerl(
                core.game.dialogue.FacialExpression.HALF_THINKING,
                "A cabbage?"
            ).also { stage++ }

            10 -> npcl(
                core.game.dialogue.FacialExpression.HAPPY,
                "Ja, cabbage is good for you!"
            ).also { stage++ }

            11 -> playerl(
                core.game.dialogue.FacialExpression.NEUTRAL,
                "Um... Thanks!"
            ).also { stage = END_DIALOGUE }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ARNOLD_LYDSPOR_3824)
}