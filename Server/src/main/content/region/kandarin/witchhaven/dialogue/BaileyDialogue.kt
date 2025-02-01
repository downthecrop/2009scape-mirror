package content.region.kandarin.witchhaven.dialogue

import content.region.kandarin.witchhaven.quest.seaslug.BaileyDialogueFile
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class BaileyDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun newInstance(player: Player): DialoguePlugin {
        return BaileyDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player!!, BaileyDialogueFile(), npc)
        return true
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BAILEY_695)
    }
}
