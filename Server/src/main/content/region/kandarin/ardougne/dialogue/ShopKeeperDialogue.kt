package content.region.kandarin.ardougne.dialogue

import content.region.kandarin.dialogue.ShopKeeperDialogue
import core.api.openNpcShop
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class ShopKeeperDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player?): DialoguePlugin {
        return content.region.kandarin.ardougne.dialogue.ShopKeeperDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(
            FacialExpression.FRIENDLY,
            "Hello, you look like a bold adventurer. You've come to the right place for adventurers' equipment."
        ).also { stage = START_DIALOGUE }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options(
                "Oh, that sounds interesting.",
                "How should I use your shop?",
                "No, sorry, I've come to the wrong place."
            ).also { stage++ }

            1 -> when (buttonId) {
                1 -> {
                    openNpcShop(player, NPCs.AEMAD_590)
                    end()
                }

                2 -> npcl(
                    FacialExpression.HAPPY,
                    "I'm glad you ask! You can buy as many of the items stocked as you wish. You can also sell most items to the shop."
                ).also { stage = END_DIALOGUE }

                3 -> npcl(
                    FacialExpression.HALF_GUILTY, "Hmph. Well, perhaps next time you'll need something from me?"
                ).also { stage = END_DIALOGUE }

            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AEMAD_590, NPCs.KORTAN_591)
    }

}