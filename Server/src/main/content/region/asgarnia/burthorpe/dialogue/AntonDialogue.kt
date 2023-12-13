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

@Initializable
class AntonDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, AntonDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return AntonDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ANTON_4295)
    }
}

class AntonDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {
        b.defaultDialogue().npcl(
            FacialExpression.ASKING,
            "Ahhh, hello there. How can I help?")
            .playerl(FacialExpression.NEUTRAL,
                "Looks like you have a good selection of weapons around here...")
            .npcl(FacialExpression.FRIENDLY,
                "Indeed so, specially imported from the finest smiths around the lands, take a look at my wares.")
            .endWith { _, player ->
                openNpcShop(player, NPCs.ANTON_4295)
                end()
            }
    }
}