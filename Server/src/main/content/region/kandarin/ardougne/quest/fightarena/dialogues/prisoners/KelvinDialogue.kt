package content.region.kandarin.ardougne.quest.fightarena.dialogues.prisoners

import core.api.isEquipped
import core.api.sendNPCDialogue
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class KelvinDialogue(player: Player? = null) : DialoguePlugin(player) {

    // Kelvin - NPC inside prison cell.
    // Source: https://runescape.wiki/w/Kelvin?oldid=2632956
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isEquipped(player, Items.KHAZARD_HELMET_74) && isEquipped(player, Items.KHAZARD_ARMOUR_75)) {
            sendNPCDialogue(player, NPCs.KELVIN_260, "Get away, get away. One day I'll have my revenge, and I'll have all your heads.", FacialExpression.ANNOYED).also { stage = END_DIALOGUE }
        } else {
            sendNPCDialogue(player, NPCs.KELVIN_260, "You're not safe here traveller. Leave while you still can", FacialExpression.FRIENDLY).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return KelvinDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KELVIN_260)
    }
}