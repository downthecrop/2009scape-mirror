package content.region.misthalin.draynor.dialogue

// import content.region.misthalin.silvarea.quest.ragandboneman.FortunatoDialogueFile
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class FortunatoDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // openDialogue(player!!, FortunatoDialogueFile(), npc)
        when (stage) {
            START_DIALOGUE -> npcl(FacialExpression.ASKING, "Can I help you at all?").also { stage++ }
            1 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "Yes, what are you selling?", 2),
                    Topic(FacialExpression.FRIENDLY, "Not at the moment", 3),
            )
            2 -> openNpcShop(player, NPCs.FORTUNATO_3671).also {
                end()
            }
            3 -> npcl(FacialExpression.ANGRY, "Then move along, you filthy ragamuffin, I have customers to serve!").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return FortunatoDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FORTUNATO_3671)
    }
}