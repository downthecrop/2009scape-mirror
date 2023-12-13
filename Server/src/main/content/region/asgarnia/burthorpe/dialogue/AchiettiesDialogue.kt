import core.api.openDialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 * @author Trident101
 */

@Initializable
class AchiettiesDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, AchiettiesDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return AchiettiesDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ACHIETTIES_796)
    }
}

class AchiettiesDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {
        b.defaultDialogue().npcl(FacialExpression.FRIENDLY,
            "Greetings. Welcome to the Heroes' Guild."
        )
    }
}