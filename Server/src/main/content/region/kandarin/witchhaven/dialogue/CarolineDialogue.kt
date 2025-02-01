package content.region.kandarin.witchhaven.dialogue

import content.region.kandarin.witchhaven.quest.seaslug.CarolineDialogueFile
import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class CarolineDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun newInstance(player: Player): DialoguePlugin {
        return CarolineDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // Fallback to default. Always the start of Sea Slug
        openDialogue(player!!, CarolineDialogueFile(), npc)
        return true
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAROLINE_696)
    }
}