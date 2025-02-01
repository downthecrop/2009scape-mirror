package content.region.kandarin.witchhaven.dialogue

import content.region.kandarin.witchhaven.quest.seaslug.HolgartPlatformDialogueFile
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

// This is to handle when Holgart is on the fishing platform.
@Initializable
class HolgartPlatformDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun newInstance(player: Player): DialoguePlugin {
        return HolgartPlatformDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // Fallback to default. Always the start of Sea Slug
        openDialogue(player!!, HolgartPlatformDialogueFile(), npc)
        return true
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HOLGART_699, NPCs.FISHERMAN_4871)
        // return intArrayOf(NPCs.HOLGART_4866)
    }
}
