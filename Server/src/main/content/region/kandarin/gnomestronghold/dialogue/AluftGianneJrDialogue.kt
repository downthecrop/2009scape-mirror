package content.region.kandarin.gnomestronghold.dialogue

import content.minigame.gnomecooking.GC_BASE_ATTRIBUTE
import content.minigame.gnomecooking.GC_TUT_PROG
import core.game.component.Component
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable

@Initializable
class AluftGianneJrDialogue(player: Player? = null) : DialoguePlugin(player) {
    var tutorialStage = -1

    override fun newInstance(player: Player?): DialoguePlugin {
        return AluftGianneJrDialogue(player)
    }

    override fun npc(vararg messages: String?): Component {
        return super.npc(FacialExpression.OLD_NORMAL,*messages)
    }

    override fun open(vararg args: Any?): Boolean {
        tutorialStage = player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_TUT_PROG",-1)
        if(tutorialStage == -1){
            player("Hey can I get a job here?").also { stage = 0 }
        } else {
            npc("Having fun?").also { stage = 1000 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc("Sure, go talk to my dad. I'll put","a good word in!").also { stage++ }
            1 -> player(FacialExpression.THINKING,"Th-thanks...?").also { stage++ }
            2 -> {
                end()
                player.setAttribute("/save:$GC_BASE_ATTRIBUTE:$GC_TUT_PROG",0)
            }

            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(4572)
    }

}