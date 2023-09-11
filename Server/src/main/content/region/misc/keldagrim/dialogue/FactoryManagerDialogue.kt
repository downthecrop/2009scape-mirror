package content.region.misc.keldagrim.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class FactoryManagerDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE -> npcl(FacialExpression.OLD_ANGRY1, "Don't bother me, can't you see I'm busy?").also { stage++ }
            1 -> playerl(FacialExpression.FRIENDLY, "No, I can't... can I go into the factory?").also { stage++ }
            2 -> npcl(FacialExpression.OLD_NORMAL, "You can go in. The Consortium, in their infinite wisdom, has decided to open up the factory to all and sundry.").also { stage++ }
            3 -> playerl(FacialExpression.FRIENDLY, "How come?").also { stage++ }
            4 -> npcl(FacialExpression.OLD_NORMAL, "We've built a new blast furnace and always have a shortage of people to work on it.").also { stage++ }
            5 -> playerl(FacialExpression.FRIENDLY, "I'll have a look, thanks.").also { stage++ }
            6 -> npcl(FacialExpression.OLD_NORMAL, "Just don't go causing any trouble, I still run this factory!").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return FactoryManagerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FACTORY_MANAGER_2171)
    }
}