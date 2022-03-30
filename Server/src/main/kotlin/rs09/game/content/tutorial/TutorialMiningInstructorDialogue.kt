package rs09.game.content.tutorial

import api.addItem
import api.getAttribute
import api.inInventory
import api.setAttribute
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Handles the mining tutor's dialogue
 * @author Ceikry
 */
@Initializable
class TutorialMiningInstructorDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return TutorialMiningInstructorDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when(getAttribute(player, "tutorial:stage", 0)) {
            30 -> npcl(FacialExpression.FRIENDLY, "Hi there. You must be new around here. So what do I call you? 'Newcomer' seems so impersonal, and if we're going to be working together, I'd rather call you by name.")
            34 -> playerl(FacialExpression.FRIENDLY, "I prospected both types of rock! One set contains tin and the other has copper ore inside.")
            35 -> {
                if(!inInventory(player, Items.BRONZE_PICKAXE_1265)) {
                    addItem(player, Items.BRONZE_PICKAXE_1265)
                    player.dialogueInterpreter.sendItemMessage(Items.BRONZE_PICKAXE_1265, "Dezzick gives you a bronze pickaxe!")
                    stage = 3
                }
                else {
                    TutorialStage.load(player, 35)
                }
            }
            40 -> playerl(FacialExpression.ASKING, "How do I make a weapon out of this?")
            41 -> {
                if(!inInventory(player, Items.HAMMER_2347)) {
                    addItem(player, Items.HAMMER_2347)
                    player.dialogueInterpreter.sendItemMessage(Items.HAMMER_2347, "Dezzick gives you a hammer!")
                    stage = 3
                }
                else
                {
                    end()
                    TutorialStage.load(player, 41)
                }
            }
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(getAttribute(player, "tutorial:stage", 0)) {
            30 -> when(stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "You can call me ${player.username}.").also { stage++ }
                1 -> npcl(FacialExpression.FRIENDLY, "Ok then, ${player.username}. My name is Dezzick and I'm a miner by Trade. Let's prospect some of these rocks.").also { stage++ }
                2 -> {
                    end()
                    setAttribute(player, "tutorial:stage", 31)
                    TutorialStage.load(player, 31)
                }
            }

            34,35 -> when(stage) {
                0 -> npcl(FacialExpression.FRIENDLY, "Absolutely right, ${player.username}. These two ore types can be smelted together to make bronze.").also { stage++ }
                1 -> npcl(FacialExpression.FRIENDLY, "So now you know what ore is in the rocks over there, why don't you have a go at mining some tin and copper? Here, you'll need this to start with.").also { stage++ }
                2 -> {
                    addItem(player, Items.BRONZE_PICKAXE_1265)
                    player.dialogueInterpreter.sendItemMessage(Items.BRONZE_PICKAXE_1265, "Dezzick gives you a bronze pickaxe!")
                    stage++
                }
                3 -> {
                    end()
                    setAttribute(player, "tutorial:stage", 35)
                    TutorialStage.load(player, 35)
                }
            }

            40,41 -> when(stage){
                0 -> npcl(FacialExpression.FRIENDLY, "Okay, I'll show you how to make a dagger out of it. You'll be needing this..").also { stage++ }
                1 -> {
                    addItem(player, Items.HAMMER_2347)
                    player.dialogueInterpreter.sendItemMessage(Items.HAMMER_2347, "Drezzick gives you a hammer!")
                    stage++
                }
                2 -> {
                    end()
                    setAttribute(player, "tutorial:stage", 41)
                    TutorialStage.load(player, 41)
                }
            }
        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MINING_INSTRUCTOR_948)
    }
}