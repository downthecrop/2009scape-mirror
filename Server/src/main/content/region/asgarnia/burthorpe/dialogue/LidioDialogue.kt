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
class LidioDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, LidioDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return LidioDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LIDIO_4293)
    }
}

class LidioDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {
        b.defaultDialogue().npcl(FacialExpression.ASKING,
            "Greetings, warrior, how can I fill your stomach today?"
        ).playerl(FacialExpression.NEUTRAL,
                "With food preferrable."
            ).endWith { _, player ->
                openNpcShop(player, NPCs.LIDIO_4293)
                end()
            }
    }
}