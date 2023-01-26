package content.region.karamja.quest.tribaltotem

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable

@Initializable
class HoracioDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        if(player.questRepository.hasStarted("Tribal Totem")){
            npcl(core.game.dialogue.FacialExpression.HAPPY,"It's a fine day to be out in a garden, isn't it? ")
            stage = 5
        }
        else{
            npcl(core.game.dialogue.FacialExpression.HAPPY,"It's a fine day to be out in a garden, isn't it? ")
            stage = 0
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> playerl(core.game.dialogue.FacialExpression.HAPPY,"Yes it's very nice.").also { stage++ }
            1 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"Days like these make me glad to be alive!").also { stage = 1000 }

            5 -> playerl(core.game.dialogue.FacialExpression.ASKING,"So... who are you?").also { stage++ }
            6 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"My name is Horacio Dobson. I'm a gardener to Lord Handlemort.").also { stage++ }
            7 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"Take a look around this beautiful garden, all of this is my handiwork.").also { stage++ }
            8 -> options("So... do you garden round the back too?","Do you need any help?").also { stage++ }
            9 -> when(buttonId){
                1 -> playerl(core.game.dialogue.FacialExpression.THINKING,"So... do you garden round the back, too?").also { stage = 10 }
                2 -> playerl(core.game.dialogue.FacialExpression.ASKING,"Do you need any help?").also { stage = 20 }
            }

            10 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"That I do!").also { stage++ }
            11 -> playerl(core.game.dialogue.FacialExpression.ASKING,"Doesn't all of the security around the house get in your way then?").also { stage++ }
            12 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"Ah. I'm used to all that. I have my keys, the guard dogs know me, and I know the combination to the door lock.").also { stage++ }
            13 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"It's rather easy, it's his middle name.").also { stage++ }
            14 -> playerl(core.game.dialogue.FacialExpression.ASKING,"Whose middle name?").also { stage++ }
            15 -> npcl(core.game.dialogue.FacialExpression.ANNOYED,"Hum. I probably shouldn't have said that. Forget I mentioned it.").also { stage = 1000 }

            20 -> npcl(core.game.dialogue.FacialExpression.ANNOYED,"Trying to muscle in on my job, eh? I'm more than happy to do this all by myself!").also { stage = 1000 }

            1000 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return HoracioDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(845)
    }
}