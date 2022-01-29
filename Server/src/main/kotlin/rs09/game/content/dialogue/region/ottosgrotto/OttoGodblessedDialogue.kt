package rs09.game.content.dialogue.region.ottosgrotto

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import rs09.tools.END_DIALOGUE

/**
 * Represents the dialogue plugin used for Otto
 * @author Splinter
 * @version 1.0
 */
@Initializable
class OttoGodblessedDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player): DialoguePlugin {
        return OttoGodblessedDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc("Good day, you seem a hearty warrior. Maybe even", "some barbarian blood in that body of yours?")
        stage = -1
        return true
    }

    override fun init() {
        super.init()
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            -1 -> options("Ask about barbarian training", "Nevermind.").also { stage++ }
            0 -> when(buttonId){
                1 -> player("Is there anything you can teach me?").also { stage = 20 }
                2 -> stage++
            }

            1 -> stage = END_DIALOGUE


            20 -> npc("I can teach you how to fish.").also { stage++ }
            21 -> player("Oh, that's pretty underwhelming. But uhhh, okay!").also { stage++ }
            22 -> npc("Alright so here's what you gotta do:","You need to grab a pole and some bait, and then","fling it into the water!").also { stage++ }
            23 -> player("The whole pole?").also { stage++ }
            24 -> npc(FacialExpression.ANGRY, "No, not the whole pole!").also { stage++ }
            25 -> npc("Look, just... grab the pole under my bed","and go click on that fishing spot.").also { stage++ }
            26 -> player(FacialExpression.ASKING,"...click?").also { stage++ }
            27 -> npc(FacialExpression.FURIOUS, "JUST GO DO IT!").also { stage++; player.setAttribute("/save:barbtraining:fishing",true) }
            28 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(2725)
    }
}