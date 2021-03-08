package core.game.content.dialogue

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable

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
            -1 -> options("Ask about hastas","Ask about barbarian training").also { stage++ }
            0 -> when(buttonId){
                1 -> player("Can you help me with Zamorakian weapons?").also { stage++ }
                2 -> player("Is there anything you can teach me?").also { stage = 20 }
            }
            1 -> {
                npc("Yes, I can convert a Zamorakian spear into a hasta.", "The spirits require me to request 300,000 coins from", "you for this service.")
                stage = 2
            }
            2 -> {
                interpreter.sendOptions("Select an Option", "Spear into hasta", "Hasta back to spear")
                stage = 3
            }
            3 -> when (buttonId) {
                1 -> if (player.inventory.contains(11716, 1) && player.inventory.contains(995, 300000)) {
                    interpreter.sendOptions("Convert your spear?", "Yes", "No")
                    stage = 4
                } else {
                    player.sendMessage("You need a Zamorakian Spear and 300,000 coins to proceed.")
                    end()
                }
                2 -> if (player.inventory.contains(14662, 1)) {
                    interpreter.sendOptions("Revert back to spear?", "Yes", "No")
                    stage = 5
                } else {
                    player.sendMessage("You need a Zamorakian Hasta to proceed.")
                    end()
                }
            }
            4 -> when (buttonId) {
                1 -> if (player.inventory.remove(Item(11716, 1), Item(995, 300000))) {
                    player.inventory.add(Item(14662))
                    interpreter.sendItemMessage(14662, "Otto converts your spear into a hasta.")
                    stage = 6
                } else {
                    end()
                }
                2 -> end()
            }
            5 -> when (buttonId) {
                1 -> if (player.inventory.remove(Item(14662, 1))) {
                    player.inventory.add(Item(11716))
                    interpreter.sendItemMessage(11716, "Otto converts your hasta back into a spear.")
                    stage = 6
                } else {
                    end()
                }
                2 -> end()
            }
            6 -> end()

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