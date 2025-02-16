package content.region.desert.quest.deserttreasure

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class IceTrollDialogue (player: Player? = null) : DialoguePlugin(player){
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player!!, IceTrollDialogueFile(), npc)
        return false
    }
    override fun newInstance(player: Player?): DialoguePlugin {
        return IceTrollDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ICE_TROLL_1935)
    }

}
class IceTrollDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {

        b.onPredicate { _ -> true }
                .npc(FacialExpression.OLD_LAUGH1, "Hur hur hur!", "Well look here, a puny fleshy human!")
                .npc(FacialExpression.OLD_LAUGH1, "You should beware of the icy wind that runs through", "this valley, it will bring a fleshy like you to a cold end", "indeed!")
                .end()
    }

}
