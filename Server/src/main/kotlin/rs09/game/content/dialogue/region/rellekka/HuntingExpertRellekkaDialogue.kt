package rs09.game.content.dialogue.region.rellekka

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.plugin.Initializable
import rs09.tools.END_DIALOGUE

/**
 * Represents the dialogue plugin used for the Hunting Expert in the Rellekkan Hunter area
 * @author Crash
 * @version 1.0
 */
@Initializable
class HuntingExpertRellekkaDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player): DialoguePlugin {
        return HuntingExpertRellekkaDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc("Good day, you seem to have a keen eye. Maybe even", "some hunter's blood in that body of yours?")
        stage = -1
        return true
    }

    override fun init() {
        super.init()
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            -1 -> options("Ask about polar hunting", "Nevermind.").also { stage++ }
            0 -> when(buttonId){
                1 -> player("Is there anything you can teach me?").also { stage = 20 }
                2 -> stage++
            }

            1 -> stage = END_DIALOGUE


            20 -> npc("I can teach you how to hunt.").also { stage++ }
            21 -> player("What kind of creatures can I hunt?").also { stage++ }
            22 -> npc("Many creatures in many ways.","You need to make some traps","and catch birds!").also { stage++ }
            23 -> player("Birds?").also { stage++ }
            24 -> npc(FacialExpression.ANGRY, "Yes, birds! Like these ones here!").also { stage++ }
            25 -> npc("Look, just... get some Hunting gear","and go set up some traps.").also { stage++ }
            26 -> player(FacialExpression.ASKING,"Is that it?").also { stage++ }
            27 -> npc(FacialExpression.FURIOUS, "JUST GO DO IT!").also { stage++ }
            28 -> end().also { player.achievementDiaryManager.finishTask(player, DiaryType.FREMENNIK,0,6) }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(5112)
    }
}