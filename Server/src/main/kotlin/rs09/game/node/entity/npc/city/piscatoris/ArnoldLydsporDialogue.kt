package rs09.game.node.entity.npc.city.piscatoris

import api.addItemOrDrop
import api.sendItemDialogue
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.global.shops.Shops
import rs09.game.node.entity.npc.other.banks.BankerNPC
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

/**
 * Provides the regular dialogue for Arnold Lydspor.
 * TODO: Swan Song quest will need a special case handling.
 */
@Initializable
class ArnoldLydsporDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        stage = START_DIALOGUE
        npc(FacialExpression.FRIENDLY, "Ah, you come back! What you want from Arnold, heh?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> options(
                "Can you open my bank account, please?",
                "I'd like to check my bank PIN settings.",
                "I'd like to collect items.",
                "Would you like to trade?",
                "Nothing, I just came to chat."
            ).also { stage++ }

            1 -> when (buttonId) {
                1 -> player("Can you open my bank account, please?")
                    .also { stage = 2 }

                2 -> player("I'd like to check my bank PIN settings.")
                    .also { stage = 3 }

                3 -> player("I'd like to collect items.")
                    .also { stage = 4 }

                4 -> player("Would you like to trade?")
                    .also { stage = 5 }

                5 -> player("Nothing, I just came to chat.")
                    .also { stage = 7 }
            }

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

            7 -> npc(
                    FacialExpression.FRIENDLY,
                    "Ah, that is nice - always I like to chat, but",
                    "Herr Caranos tell me to get back to work!",
                    "Here, you been nice, so have a present."
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

            END_DIALOGUE -> end()
        }

        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin = ArnoldLydsporDialogue(player)
    override fun getIds(): IntArray = ArnoldLydsporNPC.NPC_IDS
}