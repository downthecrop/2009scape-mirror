package content.region.kandarin.witchhaven.dialogue

import content.region.kandarin.witchhaven.quest.seaslug.KentDialogueFile
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class KentDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun newInstance(player: Player): DialoguePlugin {
        return KentDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // Fallback to default. Always the start of Sea Slug
        openDialogue(player!!, KentDialogueFile(), npc)
        return true
    }
    override fun getIds(): IntArray {
        // Base is CAROLINE_697 (Should be named KENNITH_697)
        return intArrayOf(NPCs.KENT_701)
    }
}
