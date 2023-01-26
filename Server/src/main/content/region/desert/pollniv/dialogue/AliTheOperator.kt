package content.region.desert.pollniv.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * @author qmqz
 */

@Initializable
class AliTheOperator(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.FRIENDLY,"Hello, good sir.")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(core.game.dialogue.FacialExpression.ANNOYED,"What do you want?").also { stage++ }
            1 -> player(core.game.dialogue.FacialExpression.HALF_ASKING,"I'm just new in town and have a few questions.").also { stage++ }
            2 -> npc(core.game.dialogue.FacialExpression.ASKING,"What do you want to know?").also { stage++ }

            3 -> options ("Tell me about yourself.", "Tell me about the other people in the town.", "I'm looking for Ali.").also { stage++ }
            4 -> when(buttonId){
                1 -> npc(
                    core.game.dialogue.FacialExpression.ANNOYED, "That information is available on a need to know basis",
                    "and right now, you don't need to know.").also { stage = 10 }
                2 -> npc(
                    core.game.dialogue.FacialExpression.SUSPICIOUS,"Sheep, ready for the slau... ",
                    "hang on I shouldn't be saying..., ", "listen I don't want to talk about them.").also { stage = 10 }
                3 -> npc(
                    core.game.dialogue.FacialExpression.ANNOYED,"You will have to be a lot more specific if ",
                    "you want help finding him. ", "Everyone here is called Ali.").also { stage = 30 }
            }

            10 -> npc(core.game.dialogue.FacialExpression.HALF_ASKING,"Can I help you with anything else?").also { stage++ }

            11 -> options ("Yes I'd like to ask you about something else.", "No thanks.").also { stage++ }
            12 -> when(buttonId) {
                1 -> npc(core.game.dialogue.FacialExpression.ASKING,"What do you want to know?").also { stage = 3 }
                2 -> end()
            }

            30 -> player(
                core.game.dialogue.FacialExpression.HALF_ASKING,"I've discovered that. Well he has an uncle called",
                "Ali Morrisane, a market vendor in Al Kharid and","that's all I really know about him.").also { stage++ }
            31 -> npc(
                core.game.dialogue.FacialExpression.ANNOYED,"Say no more. I too am looking for him. ",
                "The little tyke robbed me too. If we work together,", "perhaps we can catch him and teach him ","a lesson.").also { stage = 10 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return AliTheOperator(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(1902)
    }
}