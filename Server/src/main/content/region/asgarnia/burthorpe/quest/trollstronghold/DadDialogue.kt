package content.region.asgarnia.burthorpe.quest.trollstronghold

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
class DadDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (getQuestStage(player!!, TrollStronghold.questName)) {
            in 3..4 -> {
                when (stage) {
                    START_DIALOGUE -> npcl(FacialExpression.OLD_HAPPY, "What tiny human do in troll arena? Dad challenge human to fight!").also { stage++ }
                    1 -> showTopics(
                            Topic(FacialExpression.THINKING, "Why are you called Dad?", 2),
                            Topic(FacialExpression.FRIENDLY, "I accept your challenge!", 3),
                            Topic(FacialExpression.SCARED, "Eek! No thanks.", END_DIALOGUE)
                    )

                    2 -> npcl(FacialExpression.OLD_HAPPY, "Troll named after first thing try to eat!").also { stage = 1 }
                    3 -> npcl(FacialExpression.OLD_HAPPY, "Tiny human brave. Dad squish!").also { stage++ }
                    4 -> npc!!.attack(player).also {
                        npc!!.skills.lifepoints = npc!!.skills.maximumLifepoints // Reset dad to max hitpoints.
                        setQuestStage(player!!, TrollStronghold.questName, 4)
                        stage = END_DIALOGUE
                    }
                }
            }
            in 5..100 -> {
                sendMessage(player, "He doesn't seem interested in talking right now.")
            }
        }
        return true
    }
    override fun newInstance(player: Player?): DialoguePlugin {
        return DadDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DAD_1125)
    }
}