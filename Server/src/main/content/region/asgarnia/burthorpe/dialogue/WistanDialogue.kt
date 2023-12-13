package content.region.asgarnia.burthorpe.dialogue

import core.api.openDialogue
import core.api.openNpcShop
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * Represents the Wistan dialogue plugin.
 * @author 'Vexia
 * @author ovenbread
 * @author Trident101
 */
@Initializable
class WistanDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, WistanDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return WistanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WISTAN_1083)
    }
}

class WistanDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {
        b.defaultDialogue().playerl(
            FacialExpression.FRIENDLY, "Hi!"
        ).npcl(
            FacialExpression.FRIENDLY,
            "Welcome to Burthorpe Supplies. Your last shop before heading north into the mountains!"
        ).npcl(
            FacialExpression.FRIENDLY, "Would you like to buy something?"
        ).options().let { optionsBuilder ->
            optionsBuilder.option("Yes, please.").playerl(
                FacialExpression.FRIENDLY, "Yes, Please"
            ).endWith { _, player -> openNpcShop(player, NPCs.WISTAN_1083) }
            optionsBuilder.option("No, thanks.").playerl(
                FacialExpression.FRIENDLY, "No, thanks."
            ).end()
        }
    }
}