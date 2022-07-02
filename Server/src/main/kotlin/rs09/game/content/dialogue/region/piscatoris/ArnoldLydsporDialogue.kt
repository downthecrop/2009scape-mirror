package rs09.game.content.dialogue.region.piscatoris

import api.addItemOrDrop
import api.sendItemDialogue
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.Topic
import rs09.game.content.global.shops.Shops
import rs09.game.node.entity.npc.BankerNPC
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

/**
 * Provides the regular dialogue for Arnold Lydspor.
 * TODO: Swan Song quest will need a special case handling.
 *
 * @author vddCore
 */
@Initializable
class ArnoldLydsporDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.FRIENDLY, "Ah, you come back! What you want from Arnold, heh?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> showTopics(
                Topic("Can you open my bank account, please?", 2),
                Topic(FacialExpression.NEUTRAL, "I'd like to check my bank PIN settings.", 3),
                Topic(FacialExpression.NEUTRAL, "I'd like to collect items.", 4),
                Topic("Would you like to trade?", 5),
                Topic(FacialExpression.FRIENDLY, "Nothing, I just came to chat.", 7)
            )

            2 -> BankerNPC.attemptBank(player, npc)
                .also { end() }

            /* TODO change when PIN management is re-worked
             * TODO and the relevant ContentAPI call is created
             */
            3 -> player.bankPinManager.openSettings()
                .also { end() }

            4 -> BankerNPC.attemptCollect(player, npc)
                .also { end() }

            5 -> npc(FacialExpression.FRIENDLY, "Ja, I have wide range of stock...")
                .also { stage++ }

            6 -> Shops.shopsByNpc[NPCs.ARNOLD_LYDSPOR_3824]?.openFor(player)
                .also { end() }

            7 -> npcl(FacialExpression.FRIENDLY,
                    "Ah, that is nice - always I like to chat, but"
                    + " Herr Caranos tell me to get back to work!"
                    + " Here, you been nice, so have a present."
                ).also { stage++ }

            8 -> sendItemDialogue(player, Items.CABBAGE_1965, "Arnold gives you a cabbage.")
                .also {
                    addItemOrDrop(player, Items.CABBAGE_1965)
                    stage++
                }

            9 -> player(FacialExpression.HALF_THINKING, "A cabbage?")
                .also { stage++ }

            10 -> npc("Ja, cabbage is good for you!")
                 .also { stage++ }

            11 -> player(FacialExpression.NEUTRAL, "Um... Thanks!")
                 .also { stage = END_DIALOGUE }
        }

        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin = ArnoldLydsporDialogue(player)
    override fun getIds(): IntArray = ArnoldLydsporListener.NPC_IDS
}