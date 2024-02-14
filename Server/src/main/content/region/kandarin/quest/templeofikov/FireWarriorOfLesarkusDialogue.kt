package content.region.kandarin.quest.templeofikov

import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class FireWarriorOfLesarkusDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return FireWarriorOfLesarkusDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            START_DIALOGUE -> npcl(FacialExpression.ANGRY, "Who dares to enter the Temple of Ikov!").also { stage++ }
            1 -> showTopics(
                    Topic(FacialExpression.ANGRY, "A mighty hero!", 2),
                    Topic(FacialExpression.FRIENDLY, "A humble pilgrim.", 4),
            )
            2 -> npcl(FacialExpression.ANGRY, "Pathetic fool! Prepare to die!").also { stage++ }
            3 -> {
                npc!!.attack(player)
                end()
            }
            4 -> npcl(FacialExpression.ANGRY, "I haven't seen a pilgrim for thousands of years!").also { stage++ }
            5 -> npcl(FacialExpression.ANGRY, "Temple is closed!").also {
                stage = END_DIALOGUE
            }
        }
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FIRE_WARRIOR_OF_LESARKUS_277)
    }
}
